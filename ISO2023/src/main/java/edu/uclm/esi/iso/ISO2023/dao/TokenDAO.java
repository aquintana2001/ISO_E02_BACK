package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;

public interface TokenDAO extends MongoRepository<Token, String> {
	Optional<Token> findByUser(User user);
}
