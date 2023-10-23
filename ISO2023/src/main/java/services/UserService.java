package services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dao.UserDAO;
import dao.TokenDAO;
import entities.User;
import entities.Cliente;
import entities.Token;

public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TokenDAO tokenDAO;
	
	public void registrarse(String nombre, String apellidos, String email, String password, String ciudad,
			boolean carnet, String telefono, String dni) {
		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, "Ciudad Real", carnet, telefono, dni);
		
		Optional<User> userExist = this.userDAO.findByName(email);
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
			this.userDAO.save(cliente);
			this.tokenDAO.save(token);
		}else if(userExist.isPresent()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esos credenciales no pueden ser usados");
		}
	}
}
