package dao;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import entities.Administrador;
import entities.Cliente;


public interface ClienteDAO extends MongoRepository<Cliente,String>{
	Optional<Cliente> findByEmail(String email);
}
