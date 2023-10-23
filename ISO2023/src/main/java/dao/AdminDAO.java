package dao;

import java.util.Optional;

import entities.Administrador;



public interface AdminDAO {

	Optional<Administrador> findByEmail(String email);
	
}
