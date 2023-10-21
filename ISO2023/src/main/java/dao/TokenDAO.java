package dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import entities.Token;
import entities.User;


public interface TokenDAO extends JpaRepository<Token, String>{
	Optional<Token> findByUser(User user);
}

