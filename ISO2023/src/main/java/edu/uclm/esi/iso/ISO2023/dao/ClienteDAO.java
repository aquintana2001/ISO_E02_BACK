package edu.uclm.esi.iso.ISO2023.dao;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;


public interface ClienteDAO extends MongoRepository<Cliente,String>{
	Optional<Cliente> findByEmail(String email);
}
