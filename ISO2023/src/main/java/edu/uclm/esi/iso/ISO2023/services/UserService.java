package edu.uclm.esi.iso.ISO2023.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.services.SeguridadService;
import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
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
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	private AdminService adminService = new AdminService();
	private ClienteService clienteService = new ClienteService();

	public void registrarse(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, fechaNacimiento, carnet, telefono,
				dni);

		Optional<Cliente> userExist = this.clienteDAO.findByEmail(email);

		if (!comprobarSeguridad.restriccionesPassword(cliente))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");

		if (userExist.isPresent())
			throw new formatoIncompleto("Error.No puedes usar ese email.");
		userExist = this.clienteDAO.findByDni(dni);
		if (userExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");

		} else if (!userExist.isPresent()) {
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
		User possibleLogin = null;
		String errMsg = "Las contraseÃ±as no son correctas";
		String usuario= null;
		
		if (Boolean.FALSE.equals(comprobarSeguridad.emailValido(email)))
			throw new formatoIncompleto("El email no corresponde con uno vÃ¡lido");
		
		if (!possibleAdmin.isPresent() && !possibleCliente.isPresent())
			throw new numeroInvalido("No se ha podido encontrar usuario con ese mail");
	
		if (possibleAdmin.isPresent()) {
			
			boolean passwordAdmin = comprobarSeguridad.decodificador(password, possibleAdmin.get().getPassword());
		
			if (possibleAdmin.get().getIntentos() <= 0)
				throw new numeroInvalido("Este usuario estÃ¡ bloqueado debido a mÃºltiples inicios fallidos de sesiÃ³n o decisiÃ³n de un administrador. \"\r\n"
						+ "				+ \"Si necesita ayuda consulte con un administrador de la aplicaciÃ³n de TIComo\";\r\n"+ "");

			if (!passwordAdmin) {
				possibleAdmin.get().setIntentos(possibleAdmin.get().getIntentos() - 1);
				adminService.updateAdmnIntentos(possibleAdmin.get().getEmail(),possibleAdmin.get().getIntentos());
				throw new formatoIncompleto(errMsg);
			}
			possibleAdmin.get().setIntentos(5);
			adminService.updateAdmnIntentos(possibleAdmin.get().getEmail(),possibleAdmin.get().getIntentos());
			return usuario ="admin";
		}
		
		if (possibleCliente.isPresent()) {
			
			boolean passwordCliente = comprobarSeguridad.decodificador(password, possibleCliente.get().getPassword());
			if (possibleCliente.get().getIntentos() <= 0)
				throw new numeroInvalido("Este usuario estÃ¡ bloqueado debido a mÃºltiples inicios fallidos de sesiÃ³n o decisiÃ³n de un administrador. \"\r\n"
						+ "				+ \"Si necesita ayuda consulte con un administrador de la aplicaciÃ³n de TIComo\";\r\n"+ "");

			if (!passwordCliente) {
				possibleCliente.get().setIntentos(possibleCliente.get().getIntentos() - 1);
				clienteService.actualizarIntentosCliente(possibleCliente.get().getEmail(),possibleCliente.get().getIntentos());
				throw new formatoIncompleto(errMsg);
			}
			
			possibleCliente.get().setIntentos(5);
			//clienteService.actualizarIntentosCliente(possibleCliente.get().getEmail(),possibleCliente.get().getIntentos());
			return usuario ="cliente";
		}
		
		return usuario;
	}

//	public String loginUser(String email, String password) {
//		
//		Optional<Cliente> clienteExist = this.clienteDAO.findByEmail(email);
//		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
//		String usuario;
//		System.out.println("\npostman: "+password);
//		System.out.println("\ndecoded: "+adminExist.get().getPassword());
//		if (adminExist.isPresent() && this.comprobarSeguridad.decodificador(password, adminExist.get().getPassword())) {
//			return usuario = "admin";
//		} else if (clienteExist.isPresent() && this.comprobarSeguridad.decodificador(password, clienteExist.get().getPassword())) {
//			return usuario = "cliente";
//		} else {
//			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas");
//		}
//	}

}