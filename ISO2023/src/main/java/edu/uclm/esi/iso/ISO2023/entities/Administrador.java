package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id;

import edu.uclm.esi.iso.ISO2023.entities.User;

public class Administrador extends User{

	public Administrador(String nombre, String apellidos, String email, String password, boolean activo, int intentos) {
		super(nombre, apellidos, email, password, activo, intentos);
		// TODO Auto-generated constructor stub
	}
	


}
