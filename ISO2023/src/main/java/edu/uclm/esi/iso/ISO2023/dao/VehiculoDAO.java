package edu.uclm.esi.iso.ISO2023.dao;

import java.util.List;
import java.util.Optional; 

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;


public interface VehiculoDAO extends MongoRepository<Vehiculo,String>{
	Optional<Vehiculo> findById(String id);
	Optional<Vehiculo> findByMatricula(String matricula);
	List<Vehiculo> findByBateria(int bateria);
	List<Vehiculo> findByBateriaGreaterThan(int bateria);
}