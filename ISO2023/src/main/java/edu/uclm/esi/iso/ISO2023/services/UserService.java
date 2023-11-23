package edu.uclm.esi.iso.ISO2023.services;

import java.util.Optional;

import org.openqa.selenium.NotFoundException;

//import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
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
	
	
	
	private final String username = "your-username";
    private final String password = "your-password";
    private final String host = "your-mail-host";
    private final String port = "your-mail-port";
    
    
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	
	@Autowired
	private EmailService emailService;

	

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
	
	
	
	
	


	public void olvidarContrasena(String email) throws contraseñaIncorrecta, formatoIncompleto {
		
		Cliente possibleCliente = this.clienteDAO.findByEmail(email).get();
		Optional<Token> tokenAux = Optional.ofNullable(this.tokenDAO.findByUserEmail(email).get(0));
		
		if (possibleCliente==null)
            throw new formatoIncompleto("No existe un usuario con ese correo electrónico");
		
		
        String resetUrl1 = "http://localhost:4200/restablecerContrasena?token=" + comprobarSeguridad.cifrarPassword(tokenAux.get().getId());
        this.emailService.sendCorreoConfirmacion(possibleCliente, resetUrl1);
		
	}
	
//	public void olvidarContrasena(String email) {
//		Optional<Cliente> clienteExist = clienteDAO.findByEmail(email);
//		Optional<Token> tokenAux = Optional.ofNullable(this.tokenDAO.findByUserEmail(email).get(0));
//        if (clienteExist == null) {
//            throw new Exception("Usuario no encontrado");
//        }
//
//        // Genera un token y lo asocia con el usuario 
//        Token token = new Token();
//        token.setCliente(clienteExist.get());
//        token.setHoraCreacion(System.currentTimeMillis());
//        // Configura la relación inversa
//        clienteExist.setToken(tokenAux.get());
//        clienteDAO.save(clienteExist);
//
//        // Envía el correo con el token
//        emailService.enviarCorreoRecuperacionContrasena(email, tokenAux);
//    }
//	
	
//	
//	
//	 private boolean validarToken(Token token, String tokenValue) {
//	        // Implementa la lógica de validación del token según tus requisitos
//	        // Aquí asumo que el valor del token se almacena en el objeto Token
//	        return token != null && tokenValue.equals(token.getId());
//	    }
//	
//	
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
//	public void restablecerContrasena(String email, String token, String nuevaContrasena) {
//		 Optional<Cliente> cliente = clienteDAO.findByEmail(email);
//	        if (cliente == null) {
//	            throw new Exception("Cliente no encontrado");
//	        }
//
//	        Optional<Token> tokenEntity = tokenDAO.findByCliente(cliente);
//	        if (tokenEntity.isPresent() && validarToken(tokenEntity.get(), token)) {
//	            cliente.setPassword(passwordEncoder.encode(nuevaContrasena));
//	            clienteDAO.save(cliente);
//	        } else {
//	            throw new Exception("Token inválido");
//	        }
//	}
//	
//	
	
//		    
//	public void enviarCorreoRestablecimiento(String email, String token) {
//	
//	    SimpleMailMessage emailMessage = new SimpleMailMessage();
//	    emailMessage.setTo(email);
//	    emailMessage.setFrom("tucorreo@gmail.com");
//	    emailMessage.setSubject("Restablecimiento de contraseña");
//	    emailMessage.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" + token);
//	    
//	    
//
//	    emailService.getMailSender().send(emailMessage);
//	}

    

//	public void createPasswordResetToken(String email, String token) {
//        // Valida que la dirección de correo electrónico sea válida
//        if (!Validator.isEmailValid(email)) {
//            throw new Exception("La dirección de correo electrónico no es válida");
//        }
//
//        // Valida que el usuario exista
//        User user = userDAO.findByEmail(email);
//        if (user == null) {
//            throw new NotFoundException("El usuario no existe");
//        }
//
//        user.setPasswordResetToken(token);
//        userDAO.save(user);
//    }
//
//    public void sendForgotPasswordEmail(String email, String token) {
//        // Valida que el token de recuperación de contraseña no sea válido
//        if (token == null || token.isEmpty()) {
//            throw new InvalidRequestException("El token de recuperación de contraseña no es válido");
//        }
//
//        emailService.sendForgotPasswordEmail(email, token);
//    }

	

}