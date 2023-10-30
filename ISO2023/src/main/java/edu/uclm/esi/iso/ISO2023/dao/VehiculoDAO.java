package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional; 

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;


public interface VehiculoDAO extends MongoRepository<Vehiculo,String>{
	Optional<Vehiculo> findById(String id);
}