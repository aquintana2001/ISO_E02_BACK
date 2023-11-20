package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Parametros;


public interface ParametrosDAO extends MongoRepository<Parametros, String>{
	Optional<Parametros> findById(String id);
}
