package edu.uclm.esi.iso.ISO2023.services;

import java.io.IOException; 
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;
@Service
public class UserService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private TokenDAO tokenDAO;
	
	public void registrarse(String nombre, String apellidos, String email, String password, String ciudad,
			String carnet, String telefono, String dni) {
		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, "Ciudad Real", carnet, telefono, dni);
		
		Optional<Cliente> userExist = this.clienteDAO.findByEmail(email);
		
		if(!cliente.pwdSecure(password))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");
		
		if(userExist.isPresent()) {
			Optional<Token> tokenExist = this.tokenDAO.findByUser(cliente);
			if (tokenExist.isPresent()) {
				long hora = System.currentTimeMillis();
				if (hora - tokenExist.get().getHoraCreacion() < 1000*60*60*24)
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Debes validar tu cuenta");
				this.tokenDAO.delete(tokenExist.get());
				Token token= new Token();
				token.setUser(cliente);
				this.tokenDAO.save(token);
			}
		}else if(!userExist.isPresent()) {
			Token token= new Token();
			token.setUser(cliente);
			this.clienteDAO.save(cliente);
			this.tokenDAO.save(token);
		}else if(userExist.isPresent()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esos credenciales no pueden ser usados");
		}
	}
	
	
}
