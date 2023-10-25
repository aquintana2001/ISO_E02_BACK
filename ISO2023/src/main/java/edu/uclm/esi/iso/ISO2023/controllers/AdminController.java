package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.*;    

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

<<<<<<< HEAD:ISO2023/src/main/java/edu/uclm/esi/iso/ISO2023/controllers/AdminController.java
import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.User;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import edu.uclm.esi.iso.ISO2023.exceptions.*;
import edu.uclm.esi.iso.ISO2023.services.AdminService;
=======
import dao.AdminDAO;
import dao.ClienteDAO;
import dao.VehiculoDAO;
import entities.Administrador;
import entities.Cliente;
import entities.Coche;
import entities.Moto;
import entities.Patinete;
import entities.User;
import entities.Vehiculo;
import exceptions.*;
import services.AdminService;
>>>>>>> alejandro:ISO2023/src/main/java/controllers/AdminController.java

@RestController
@RequestMapping("admin")
@CrossOrigin("*")
public class AdminController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private VehiculoService vehiculoService;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	
	
	
	
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
	

//	@PutMapping("/modificarCliente")
//	public Cliente updateCliente(@PathVariable String email, @RequestBody Map<String, Object> info) {
//		Cliente cliente;
//		if(cliente.pwdSecure(cliente.getPassword())) {
//			if(cliente.comprobarDni(cliente.getDni())) {
//				if(cliente.comprobarNumero(cliente.getTelefono())) {
//					Cliente clienteBBDD = clienteDAO.findByEmail(email).get();
//					clienteBBDD.setNombre(cliente.getNombre());
//					clienteBBDD.setApellidos(cliente.getApellidos());
//					clienteBBDD.setEmail(cliente.getEmail());
//					clienteBBDD.setPassword(cliente.getPassword());
//					clienteBBDD.setActivo(cliente.getActivo());
//					clienteBBDD.setIntentos(cliente.getIntentos());
//					clienteBBDD.setCarnet(cliente.getCarnet());
//					clienteBBDD.setTelefono(cliente.getTelefono());
//					clienteBBDD.setDni(cliente.getDni());
//				}
//			}
//		}else {
//			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ha sucedido un error, no se cumple con los requisitos de nuestra politica de contraseñas");
//		}
//		this.ClientService.actualizar(nombre, Apellidos, Email, Password, Ciudad, Carnet, Telefono, Dni);
//	}

	@PutMapping("/modificarAdminstrador")
	public Administrador updateAdmin(@PathVariable String email, @RequestBody Map<String, Object> info) {
		User admin = null;
		if(admin.pwdSecure(admin.getPassword())) {
			Administrador administradorBBDD = adminDAO.findByEmail(email).get();
			administradorBBDD.setNombre(admin.getNombre());
			administradorBBDD.setApellidos(admin.getApellidos());
			administradorBBDD.setEmail(admin.getEmail());
			administradorBBDD.setPassword(admin.getPassword());
			
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ha sucedido un error, no se cumple con los requisitos de nuestra politica de contraseñas");
		}
		try {
			adminService.actualizarAdmin(admin);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return null;
	}
	
	
	
	@DeleteMapping("")
	public ResponseEntity<String> deleteAdmin(@PathVariable String email) {
		Administrador admin = adminDAO.findByEmail(email).get();
		adminDAO.delete(admin);
		return ResponseEntity.ok("Administrador elimnado correctamente");
	
		
	}
	
	
//	@DeleteMapping("")  //cambiar atributo activa a false
//	public ResponseEntity<String> darDeBajaUserAdmin(@PathVariable String email) {
//		Cliente cliente = clienteDAO.findByEmail(email).get();
//		
//		if(cliente!=null) {
//			cliente.setActivo(false);
//			clienteDAO.save(cliente);
//			return ResponseEntity.ok("Cliente dado de baja corrrectamente.");
//		}else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hemos detectado ningun cliente.");
//			
//		}
//	
//	
//	}
	
	
<<<<<<< HEAD:ISO2023/src/main/java/edu/uclm/esi/iso/ISO2023/controllers/AdminController.java
	
//
//	public Vehiculo darAltaV() {
//		return null;
//	}
//
//	public Vehiculo darBajaV() {
//		return null;
//	}
//
//	public Vehiculo consultarVehiculos() {
//		return null;
//	}
//	
//	@PostMapping("/updateAdminIntentos")
//	public void updateAdminIntentos(String email, int intentos) {
//		try {
//			adminService.actualizarIntentosAdmin(email,intentos);
//		}catch(Exception e) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//		}
//	}
//	
=======

	@PostMapping("/darAltaVehiculo")
	public ResponseEntity<String> darAltaVehiculo( @RequestBody Map<String, Object> info) {

		String email =info.get("email").toString();
		Optional<Administrador> adminExist = adminDAO.findByEmail(email);


		if (adminExist.isPresent()) {
			String tipoVehiculo = (String) info.get("tipo");


			switch (tipoVehiculo) {
			case "coche":
				darAltaCoche(info);
				break;
			case "moto":
				darAltaMoto(info);
				break;
			case "patinete":
				darAltaPatinete(info);
				break;
			default:
				return ResponseEntity.badRequest().body("Tipo de vehículo desconocido.");
			}
			return ResponseEntity.ok("Vehículo dado de alta con éxito.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para dar de alta vehículos.");
		}
	} 


	private  void darAltaCoche(Map<String, Object> info) {

		
		String matricula = info.get("matricula").toString();
		String tipo = info.get("tipo").toString();
		int bateria = Integer.parseInt(info.get("bateria").toString());
		String modelo = info.get("modelo").toString();
		String estado = info.get("estado").toString();
		String direccion = info.get("direccion").toString();
		int nPlazas = Integer.parseInt(info.get("nPlazas").toString());
		
		this.vehiculoService.darAltaCoche();		
		
	}


	private void darAltaMoto(Map<String, Object> info) {
		
		
		String matricula = info.get("matricula").toString();
		String tipo = info.get("tipo").toString();
		int bateria = Integer.parseInt(info.get("bateria").toString());
		String modelo = info.get("modelo").toString();
		String estado = info.get("estado").toString();
		String direccion = info.get("direccion").toString();
		String casco = info.get("casco").toString();
		
		this.vehiculoService.darAltaMoto();
	}


	private void darAltaPatinete(Map<String, Object> info) {
		
		String matricula = info.get("matricula").toString();
		String tipo = info.get("tipo").toString();
		int bateria = Integer.parseInt(info.get("bateria").toString());
		String modelo = info.get("modelo").toString();
		String estado = info.get("estado").toString();
		String direccion = info.get("direccion").toString();
		String color = info.get("color").toString();
		
		this.vehiculoService.darAltaPatinete();
	}
	
	
	
	
	
	
	@DeleteMapping
	public ResponseEntity<String>  darBajaVehiculo(@PathVariable String id) {
		Vehiculo vehiculo = vehiculoDAO.findById(id).get();
		vehiculoService.delete(vehiculo);
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
	
>>>>>>> alejandro:ISO2023/src/main/java/controllers/AdminController.java
	
}
