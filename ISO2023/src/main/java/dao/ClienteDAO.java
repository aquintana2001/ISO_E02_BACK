package dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entities.Cliente;


public interface ClienteDAO extends JpaRepository<Cliente, String>{
	Optional<Cliente> findByName(String name);
}
