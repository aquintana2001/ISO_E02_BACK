package dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entities.Vehiculo;


public interface VehiculoDAO extends JpaRepository<Vehiculo, String>{
	Optional<Vehiculo> findByMatricula(String matricula);
}