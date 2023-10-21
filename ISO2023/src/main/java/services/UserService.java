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
import entities.Token;

public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TokenDAO tokenDAO;
	
	public void registrarse(String nombre, String apellidos, String email, String password) {
		User user = new User();
		user.setNombre(nombre);
		user.setApellidos(apellidos);
		user.setEmail(email);
		user.setPassword(password);
		user.setActivo(true);
		user.setIntentos(5);
		
		Optional<User> userExist = this.userDAO.findByName(email);
		if(userExist.isPresent()) {
			Optional<Token> tokenExist = this.tokenDAO.findByUser(user);
			if (tokenExist.isPresent()) {
				long hora = System.currentTimeMillis();
				if (hora - tokenExist.get().getHoraCreacion() < 1000*60*60*24)
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Debes validar tu cuenta");
				this.tokenDAO.delete(tokenExist.get());
				Token token= new Token();
				token.setUser(user);
				this.tokenDAO.save(token);
			}
		}else if(!userExist.isPresent()) {
			Token token= new Token();
			token.setUser(user);
			this.userDAO.save(user);
			this.tokenDAO.save(token);
		}else if(userExist.isPresent()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esos credenciales no pueden ser usados");
		}
	}
}
