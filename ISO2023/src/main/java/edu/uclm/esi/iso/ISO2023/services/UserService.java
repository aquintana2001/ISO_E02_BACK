package edu.uclm.esi.iso.ISO2023.services;


import java.io.IOException;
import java.util.Optional; 


//import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.zxing.WriterException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.MantenimientoDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Mantenimiento;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

@Service
public class UserService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private MantenimientoDAO mantenimientoDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	
	private static final String ADMIN = "admin";
	private static final String CLIENTE = "cliente";
	private static final String MANTENIMIENTO = "mantenimiento";



	public byte[] registrarse(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido, WriterException, IOException {

		Cliente cliente = new Cliente(nombre, apellidos, email, password, false, 5, fechaNacimiento, carnet, telefono,
				dni,"");

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

			if (Boolean.FALSE.equals((comprobarSeguridad.comprobarDni(cliente.getDni()))))
				throw new numeroInvalido(
						"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

			if (cliente.getPassword().length() != 60) {
				cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
			}
		
			cliente.setsecretKey(comprobarSeguridad.generateSecretKey());
			
			this.clienteDAO.save(cliente);
			this.tokenDAO.save(token);
		}
		return comprobarSeguridad.generateQRCodeImage(cliente.getsecretKey(), cliente.getEmail());
	}
	
	public void confirmarRegister(String email, int mfaKey) throws formatoIncompleto, WriterException, IOException {
		Optional<Cliente> clienteExist = this.clienteDAO.findByEmail(email);
		String errMsg = "Credenciales incorrectos";
		if (!clienteExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");
		}
		
		boolean mfa = comprobarSeguridad.verifyCode(clienteExist.get().getsecretKey(), mfaKey);
		if (!mfa) {
			throw new formatoIncompleto(errMsg);	
		}
		clienteExist.get().setActivo(true);
		clienteDAO.save(clienteExist.get());
	}



	public String loginUser(String email, String password, int mfaKey) throws formatoIncompleto, numeroInvalido {
		String tipoUsuario = checkUser(email, password);
		User usuario = findUser(tipoUsuario, email);
		boolean passwordUser = comprobarSeguridad.decodificador(password, usuario.getPassword());

		if (Boolean.FALSE.equals(comprobarSeguridad.validarEmail(email)))
			throw new formatoIncompleto("");

		if (usuario.getIntentos() <= 0 || Boolean.FALSE.equals(usuario.getActivo())) {
			usuario.setActivo(false);
			saveUser(usuario, tipoUsuario);
		}
		if (!passwordUser) {
			usuario.setIntentos(usuario.getIntentos() - 1);
			saveUser(usuario, tipoUsuario);
		} else {
			usuario.setIntentos(5);
			saveUser(usuario, tipoUsuario);
			if (tipoUsuario.equals(CLIENTE) ) {
				if (!checkmfaKey(usuario.getEmail(), mfaKey)) {
					throw new numeroInvalido("No coinciden los codigos");
				}
			}
		}
		
		return tipoUsuario;
	}
	
	public boolean checkmfaKey(String email, int mfaKey) {
		Optional<Cliente> clienteExist = clienteDAO.findByEmail(email);
		boolean comprobar = comprobarSeguridad.verifyCode(clienteExist.get().getsecretKey(), mfaKey);
		return comprobar;
	}

	public String checkUser(String email, String password) {
		String usuario = "";
		Optional<Administrador> adminExist = adminDAO.findByEmail(email);
		Optional<Cliente> clienteExist = clienteDAO.findByEmail(email);
		Optional<Mantenimiento> mantExist = mantenimientoDAO.findByEmail(email);
		if (adminExist.isPresent() && comprobarSeguridad.decodificador(password, adminExist.get().getPassword())) {
			usuario = ADMIN;
		} else if (clienteExist.isPresent() && comprobarSeguridad.decodificador(password, clienteExist.get().getPassword())) {
			usuario = CLIENTE;
		} else if (mantExist.isPresent() && comprobarSeguridad.decodificador(password, mantExist.get().getPassword())) {
			usuario = MANTENIMIENTO;
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos.");
		}
		return usuario;
	}

	public User findUser(String tipoUsuario, String email) {
		User usuario = null;
		Optional<Administrador> admin = this.adminDAO.findByEmail(email);
		Optional<Cliente> cliente = this.clienteDAO.findByEmail(email);
		Optional<Mantenimiento> mantenimiento = this.mantenimientoDAO.findByEmail(email);

		switch (tipoUsuario) {
		case ADMIN:
			if(admin.isPresent())
				usuario = admin.get();
			break;
		case CLIENTE:
			if(cliente.isPresent())
				usuario = cliente.get();
			break;
		case MANTENIMIENTO:
			if(mantenimiento.isPresent())
				usuario = mantenimiento.get();
			break;
		default:
			return usuario;
		}
		return usuario;
	}

	public void saveUser(User usuario, String tipoUsuario) {
		switch (tipoUsuario) {
		case ADMIN:
			this.adminDAO.save((Administrador) usuario);
			break;
		case CLIENTE:
			this.clienteDAO.save((Cliente) usuario);
			break;
		case MANTENIMIENTO:
			this.mantenimientoDAO.save((Mantenimiento) usuario);
			break;
		default:
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al guardar el usuario en la BBDD.");

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
}