package edu.uclm.esi.iso.ISO2023.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uclm.esi.iso.ISO2023.entities.Reserva;

public interface ReservaDAO extends MongoRepository<Reserva, String>{
	Optional<Reserva> findById(String id);
	List<Reserva> findByUsuarioEmail(String emailCliente);
	List<Reserva> findByUsuarioEmailAndVehiculoEstado(String emailUsuario, String estadoVehiculo);
	List<Reserva> findByUsuarioEmailAndFechaBetweenAndEstado(String emailUsuario, String primerDia, String ultimoDia, String estado);
}
