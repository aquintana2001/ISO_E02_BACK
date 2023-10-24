package controllers;

import java.util.*;   

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import dao.AdminDAO;


import entities.Vehiculo;
import services.AdminService;

@RestController
public class AdminController {
	@Autowired
	private AdminService adminService;
	private AdminDAO adminDAO;
	
	
	
	
	@PostMapping("/register")
	public void registrarse(@RequestBody Map<String, Object> info) {
		String tipo = info.get("tipo").toString();
		String password1 = info.get("password1").toString();
		String passwordd2 = info.get("password2").toString();
		if (!password1.equals(passwordd2))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
		
		String nombre= info.get("nombre").toString();
		String apellidos = info.get("apellidos").toString();
		String email = info.get("email").toString();
		String ciudad = info.get("ciudad").toString();
		boolean carnet = Boolean.parseBoolean(info.get("carnet").toString());
		String telefono = info.get("telefono").toString();
		String dni = info.get("dni").toString();
		
		try {
			this.adminService.registrarse(nombre, apellidos, email, password1);
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		
	}
	
	
//	@PutMapping("/modificarUsuario")
//	public Administrador updateAdmin(@PathVariable String email, @RequestBody Map<String, Object> info) {
//		if(admin.pwdSecure(admin.getPassword())) {
//			Administrador administradorBBDD = adminDAO.findByEmail(email);
//			administradorBBDD.setNombre(admin.getNombre());
//			administradorBBDD.setApellidos(admin.getApellidos());
//			administradorBBDD.setEmail(admin.getEmail());
//			administradorBBDD.setCiudad(admin.getCiudad());
//			administradorBBDD.setCarnet(admin.getCarnet());
//			administradorBBDD.setTelefono(admin.getTelefono());
//			administradorBBDD.setDni(admin.getDni());
//			
//			administradorBBDD.setPassword(admin.getPassword());
//			
//		}else {
//			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ha sucedido un error, no se cumple con los requisitos de nuestra politica de contraseñas");
//		}
//		this.adminService.actualizar(nombre, Apellidos, Email, Password, Ciudad, Carnet, Telefono, Dni);
//	}
//	
	
//	@DeleteMapping("")
//	public void deleteAdmin(@PathVariable String email) {
//		Administrador admin = adminDAO.findByEmail(email);
//		adminDAO.delete(admin);
//	}
//	
//	
	

	public Vehiculo darAltaV() {
		return null;
	}

	public Vehiculo darBajaV() {
		return null;
	}

	public Vehiculo consultarVehiculos() {
		return null;
	}
	
	@PostMapping("/updateAdminIntentos")
	public void updateAdminIntentos(String email, int intentos) {
		try {
			adminService.actualizarIntentosAdmin(email,intentos);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
}
