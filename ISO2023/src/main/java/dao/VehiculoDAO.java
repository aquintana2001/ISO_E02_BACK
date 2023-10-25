package dao;

import java.util.Optional; 

import org.springframework.data.mongodb.repository.MongoRepository;

import entities.Administrador;
import entities.Vehiculo;


public interface VehiculoDAO extends MongoRepository<Vehiculo,String>{
	Optional<Vehiculo> findById(int id);
}