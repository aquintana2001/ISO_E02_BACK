package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Mantenimiento;

public interface MantenimientoDAO extends MongoRepository<Mantenimiento,String>{
	Optional<Mantenimiento> findByEmail(String email);
}
