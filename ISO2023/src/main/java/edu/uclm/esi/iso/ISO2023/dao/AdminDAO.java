package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Administrador;



public interface AdminDAO extends MongoRepository<Administrador,String>{
	Optional<Administrador> findByEmail(String email);
}
