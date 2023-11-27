package edu.uclm.esi.iso.ISO2023.dao;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;


public interface TokenDAO extends MongoRepository<Token, String> {
	Optional<Token> findByUser(User user);
	
	List<Token> findByUserEmail(String email);
	void deleteAllByUserEmail(String email);	
	Optional<Token> findById(String id);
}
