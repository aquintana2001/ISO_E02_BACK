package dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


import entities.Administrador;
import entities.User;



public interface AdminDAO extends MongoRepository<Administrador,String>{
	Optional<Administrador> findByEmail(String email);

	void save(User admin);
	
}
