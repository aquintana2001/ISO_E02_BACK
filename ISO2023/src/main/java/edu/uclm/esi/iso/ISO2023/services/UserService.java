package edu.uclm.esi.iso.ISO2023.services;

import java.io.IOException; 
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;
import edu.uclm.esi.iso.ISO2023.exceptions.*;
@Service
public class UserService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private TokenDAO tokenDAO;
	private SeguridadService comprobarSeguridad = new SeguridadService();
	
	public void registrarse(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, fechaNacimiento, carnet, telefono, dni);
		
		Optional<Cliente> userExist = this.clienteDAO.findByEmail(email);
		
		if(!comprobarSeguridad.restriccionesPassword(cliente))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");
		
		if(userExist.isPresent()) 
			throw new formatoIncompleto("Error.No puedes usar ese email.");
//			Optional<Token> tokenExist = this.tokenDAO.findByUser(cliente);
//			if (tokenExist.isPresent()) {
//				long hora = System.currentTimeMillis();
//				if (hora - tokenExist.get().getHoraCreacion() < 1000*60*60*24)
//					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Debes validar tu cuenta");
//				this.tokenDAO.delete(tokenExist.get());
//				Token token= new Token();
//				token.setUser(cliente);
//				this.tokenDAO.save(token);
//			}
		
		userExist = this.clienteDAO.findById(dni);
		
		if(userExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");
		
		}else if(!userExist.isPresent()) {
			Token token= new Token();
			token.setUser(cliente);
			comprobarSeguridad.restriccionesPassword(cliente);
			comprobarSeguridad.validarEmail(cliente.getEmail());
			comprobarSeguridad.comprobarNumero(cliente.getTelefono());
			
			if(Boolean.FALSE.equals(comprobarSeguridad.comprobarDni(cliente.getDni())))
				throw new numeroInvalido("El NIF introducido no es un NIF vÃ¡lido. Tiene que contener 8 nÃºmeros y un caracter");
			
			if(cliente.getPassword().length() != 60) {
				cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
			}
			this.clienteDAO.save(cliente);
			this.tokenDAO.save(token);
		}
	}
	
	
}