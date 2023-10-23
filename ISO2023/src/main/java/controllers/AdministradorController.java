package controllers;

import java.util.*; 

import org.springframework.web.bind.annotation.RestController;

import entities.Administrador;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RestController;


import dao.AdminDAO;

import org.springframework.web.bind.annotation.RestController;

import entities.Vehiculo;
import entities.User;

@RestController

public class AdministradorController {
	@PutMapping("")
	public Administrador updateAdmin(@PathVariable String email, @RequestBody Administrador admin) {
		if(admin.pwdSecure(admin.getPassword())) {
			Administrador administradorBBDD = adminRepositorio.findByEmail(email);
			administradorBBDD.setEmail(admin.getEmail());
			administradorBBDD.setNombre(admin.getNombre());
			administradorBBDD.setApellidos(admin.getApellidos());
			administradorBBDD.setPassword(admin.getPassword());
			
			return adminRepositorio.save(administradorBBDD);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ha sucedido un error, no se cumple con los requisitos de nuestra politica de contraseñas");
		}
	}
	
	
	@DeleteMapping("")
	public void deleteAdmin(@PathVariable String email) {
		Administrador admin = adminRepositorio.findByEmail(email);
		adminRepositorio.delete(admin);
	}
	
	
	

	public Vehiculo darAltaV() {

	}

	public Vehiculo darBajaV() {

	}

	public Vehiculo consultarVehiculos() {

	}
	
	*/
}
