package dao;

import java.util.Optional; 

import org.springframework.data.mongodb.repository.MongoRepository;

import entities.Administrador;
import entities.Token;
import entities.User;


public interface TokenDAO extends MongoRepository<Token,String>{
	Optional<Token> findByUser(User user);
}

