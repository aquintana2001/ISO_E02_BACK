package edu.uclm.esi.iso.ISO2023.services;


import java.util.Optional; 

//import javax.mail.MessagingException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.exceptions.*;


@Service
public class UserService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;

//	@Autowired
//	private  EmailService emailService;


	public void registrarse(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {

		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, fechaNacimiento, carnet, telefono,
				dni);


		Optional<Cliente> userExist = this.clienteDAO.findByEmail(email);
		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);

		if (!comprobarSeguridad.restriccionesPassword(cliente))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");


		if (userExist.isPresent() || adminExist.isPresent())
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");
		userExist = this.clienteDAO.findByDni(dni);
		if (userExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");

		} else {
			Token token = new Token();

			token.setUser(cliente);
			comprobarSeguridad.restriccionesPassword(cliente);
			comprobarSeguridad.validarEmail(cliente.getEmail());
			comprobarSeguridad.comprobarNumero(cliente.getTelefono());


			if (!(comprobarSeguridad.comprobarDni(cliente.getDni())))
				throw new numeroInvalido(
						"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

			if (cliente.getPassword().length() != 60) {

				cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
			}
			this.clienteDAO.save(cliente);
			this.tokenDAO.save(token);
		}
	}


	public String loginUser(String email, String password) throws formatoIncompleto, numeroInvalido {
		Optional<Administrador> possibleAdmin = this.adminDAO.findByEmail(email);
		Optional<Cliente> possibleCliente = this.clienteDAO.findByEmail(email);
		String errMsg = "Credenciales incorrectos";
		String usuario = null;

		if (Boolean.FALSE.equals(comprobarSeguridad.validarEmail(email)))
			throw new formatoIncompleto(errMsg);

		if (!possibleAdmin.isPresent() && !possibleCliente.isPresent())
			throw new numeroInvalido(errMsg);

		if (possibleAdmin.isPresent()) {

			boolean passwordAdmin = comprobarSeguridad.decodificador(password, possibleAdmin.get().getPassword());

			if (possibleAdmin.get().getIntentos() <= 0 || !possibleAdmin.get().getActivo()) {
				possibleAdmin.get().setActivo(false);
				adminDAO.save(possibleAdmin.get());
				throw new numeroInvalido("Este usuario esta bloqueado.");
			}
			if (!passwordAdmin) {
				possibleAdmin.get().setIntentos(possibleAdmin.get().getIntentos() - 1);
				adminDAO.save(possibleAdmin.get());
				throw new formatoIncompleto(errMsg);
			}
			possibleAdmin.get().setIntentos(5);
			adminDAO.save(possibleAdmin.get());
			usuario = "admin";
		}

		if (possibleCliente.isPresent()) {

			boolean passwordCliente = comprobarSeguridad.decodificador(password, possibleCliente.get().getPassword());
			if (possibleCliente.get().getIntentos() <= 0 || !possibleCliente.get().getActivo()) {
				possibleAdmin.get().setActivo(false);
				adminDAO.save(possibleAdmin.get());
				throw new numeroInvalido("Este usuario esta bloqueado.");
			}
			if (!passwordCliente) {
				possibleCliente.get().setIntentos(possibleCliente.get().getIntentos() - 1);
				clienteDAO.save(possibleCliente.get());
				throw new formatoIncompleto(errMsg);
			}

			possibleCliente.get().setIntentos(5);
			clienteDAO.save(possibleCliente.get());
			usuario = "cliente";
		}

		return usuario;
	}
	
	public String comprobarUsuario(String email, String password) {
		String usuario="";
		Optional<Administrador> adminExist = adminDAO.findByEmail(email);
		Optional<Cliente> clienteExist = clienteDAO.findByEmail(email);
		if(adminExist.isPresent()&&comprobarSeguridad.decodificador(password, adminExist.get().getPassword())) {
			usuario = "admin";
		}else if(clienteExist.isPresent()&&comprobarSeguridad.decodificador(password, clienteExist.get().getPassword())){
			usuario = "cliente";
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta accion.");
		}
		return usuario;
	}


//	public void olvidarContrasena(String email) {
//		
//		Optional<Cliente> possibleCliente = this.clienteDAO.findByEmail(email);
//		Optional<Token> tokenAux = Optional.ofNullable(this.tokenDAO.findByUserEmail(email).get(0));
//		
//		if (possibleCliente == null) {
//            return;
//        }
//		
//		
//        String resetUrl1 = "http://localhost:4200/restablecerContrasena?token=" + comprobarSeguridad.cifrarPassword(tokenAux.get().getId());
//        enviarCorreoRestablecimiento(possibleCliente.get().getEmail(), resetUrl1);
//		
//	}
//	
//	
//	public boolean validarToken(String tokenValue) {
//		
//        Token token = tokenDAO.findById(tokenValue).orElse(null);
//        return token != null;
//    }
//	
//	public void restablecerContrasena(String tokenValue, String nuevaContrasena) {
//        
//        if (validarToken(tokenValue)) {
//
//            Token token = tokenDAO.findById(tokenValue).orElse(null);
//            Cliente cliente = token.getCliente();
//            cliente.setPassword(comprobarSeguridad.cifrarPassword(nuevaContrasena));
//            tokenDAO.delete(token);
//            clienteDAO.save(cliente);
//        }
//        
//        
//    }
//	
//	
//	public void enviarCorreoRestablecimiento(String email, String token){
//        
//        String sender = "PracticaISO2324@outlook.es";
//        String password = "Admin12345*";
//
//        EmailService.Configuracion configuracion = new EmailService.Configuracion(sender, password);
//
//        String subject = "Restablecimiento de contraseña";
//        String body = "Haga clic en el siguiente enlace para restablecer su contraseña: " +
//                "https://www.example.com/restablecerContrasena?token=" + token;
//
//        try {
//			emailService.send(configuracion, email, subject, body);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

}