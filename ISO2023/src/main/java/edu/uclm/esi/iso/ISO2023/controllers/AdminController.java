package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Parametros;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import edu.uclm.esi.iso.ISO2023.exceptions.numeroInvalido;
import edu.uclm.esi.iso.ISO2023.services.AdminService;
import edu.uclm.esi.iso.ISO2023.services.ClienteService;
import edu.uclm.esi.iso.ISO2023.services.VehiculoService;

@RestController
@RequestMapping("admin")
@CrossOrigin("*")
public class AdminController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private VehiculoService vehiculoService;

	public static final String NOMBRE = "nombre";
	public static final String PASSWORD = "password";
	public static final String APELLIDOS = "apellidos";
	public static final String EMAIL = "email";
	public static final String BATERIA = "bateria";
	public static final String DIRECCION = "direccion";
	public static final String ESTADO = "estado";
	public static final String MATRICULA = "matricula";
	public static final String MODELO = "modelo";
	public static final String TIPO = "tipo";
	public static final String DISPONIBLE = "disponible";
	public static final String EMAILUSER = "emailUser";
	public static final String PASSWORDUSER = "passwordUser";

	@PostMapping("/cliente")
	public List<Cliente> listaCliente(@RequestBody Map<String, Object> info) {
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		return clienteService.listaClientes(emailAdmin, passwordAdmin);
	}

	@PostMapping("/vehiculo")
	public List<Vehiculo> listaVehiculo(@RequestBody Map<String, Object> info) {
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		return vehiculoService.listaVehiculo(emailAdmin, passwordAdmin);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registrarse(@RequestBody Map<String, Object> info) {
		String password1 = info.get("password1").toString();
		String password2 = info.get("password2").toString();
		if (!password1.equals(password2))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");

		String nombre = info.get(NOMBRE).toString();
		String apellidos = info.get(APELLIDOS).toString();
		String email = info.get(EMAIL).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		String tipoUsuario = info.get("tipoUsuario").toString();

		try {
			if(tipoUsuario.equals("admin"))
				this.adminService.registrarse(nombre, apellidos, email, password1, emailAdmin, passwordAdmin, tipoUsuario);
			if(tipoUsuario.equals("mantenimiento"))
				this.adminService.registrarse(nombre, apellidos, email, password1, emailAdmin, passwordAdmin, tipoUsuario);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Registro realizado con éxito.");
	}

	@PutMapping("/actualizarCliente")
	public ResponseEntity<String> actualizarCliente(@RequestBody Map<String, Object> info) {
		String nombre = info.get(NOMBRE).toString();
		String apellidos = info.get(APELLIDOS).toString();
		String email = info.get(EMAIL).toString();
		String password = info.get(PASSWORD).toString();
		boolean activo = Boolean.parseBoolean(info.get("activo").toString());
		int intentos = Integer.parseInt(info.get("intentos").toString());
		String fechaNacimiento = info.get("fechaNacimiento").toString();
		String carnet = info.get("carnet").toString();
		String telefono = info.get("telefono").toString();
		String dni = info.get("dni").toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();

		try {
			this.clienteService.actualizarCliente(nombre, apellidos, email, password, activo, intentos, fechaNacimiento,
					carnet, telefono, dni, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Actualizacion realizada con éxito.");
	}

	@DeleteMapping("/eliminarAdmin")
	public ResponseEntity<String> eliminarAdmin(@RequestBody Map<String, Object> info) {
		String email = info.get(EMAIL).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		try {
			this.adminService.eliminarAdmin(email, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Administrador eliminado con éxito.");
	}

	@DeleteMapping("/eliminarCliente")
	public ResponseEntity<String> eliminarCliente(@RequestBody Map<String, Object> info) {
		String email = info.get(EMAIL).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		try {
			this.clienteService.eliminarCliente(email, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Cliente eliminado con éxito.");
	}

	@PostMapping("/darAltaVehiculo")
	public ResponseEntity<String> darAltaVehiculo(@RequestBody Map<String, Object> info) throws numeroInvalido {

		String tipoVehiculo = (String) info.get(TIPO);

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

	}

	private void darAltaCoche(Map<String, Object> info) throws numeroInvalido {
		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = 100;
		String modelo = info.get(MODELO).toString();
		String estado = DISPONIBLE;
		String direccion = info.get(DIRECCION).toString();
		int nPlazas = Integer.parseInt(info.get("nPlazas").toString());
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();

		this.vehiculoService.darAltaCoche(matricula, tipo, bateria, modelo, estado, direccion, nPlazas, emailAdmin,
				passwordAdmin);

	}

	private void darAltaMoto(Map<String, Object> info) throws numeroInvalido {

		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = 100;
		String modelo = info.get(MODELO).toString();
		String estado = DISPONIBLE;
		String direccion = info.get(DIRECCION).toString();
		boolean casco = Boolean.parseBoolean(info.get("casco").toString());
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();

		this.vehiculoService.darAltaMoto(matricula, tipo, bateria, modelo, estado, direccion, casco, emailAdmin,
				passwordAdmin);
	}

	private void darAltaPatinete(Map<String, Object> info) throws numeroInvalido {

		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = 100;
		String modelo = info.get(MODELO).toString();
		String estado = DISPONIBLE;
		String direccion = info.get(DIRECCION).toString();
		String color = info.get("color").toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();

		this.vehiculoService.darAltaPatinete(matricula, tipo, bateria, modelo, estado, direccion, color, emailAdmin,
				passwordAdmin);
	}

	@DeleteMapping("/darBajaVehiculo")
	public ResponseEntity<String> darBajaVehiculo(@RequestBody Map<String, Object> info) {
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		String id = info.get("id").toString();
		vehiculoService.darBajaVehiculo(id, emailAdmin, passwordAdmin);
		return ResponseEntity.ok("Vehículo dado de baja con éxito.");
	}

	@PutMapping("/actualizarVehiculo")
	public ResponseEntity<String> actualizarVehiculo(@RequestBody Map<String, Object> info) {
		String tipoVehiculo = (String) info.get(TIPO);

		switch (tipoVehiculo) {
		case "coche":
			modificarCoche(info);
			break;
		case "moto":
			modificarMoto(info);
			break;
		case "patinete":
			modificarPatinete(info);
			break;
		default:
			return ResponseEntity.badRequest().body("Tipo de vehículo desconocido.");
		}
		return ResponseEntity.ok("Vehículo modificado con éxito.");
	}

	public void modificarCoche(Map<String, Object> info) {
		String id = info.get("id").toString();
		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = Integer.parseInt(info.get(BATERIA).toString());
		String modelo = info.get(MODELO).toString();
		String estado = info.get(ESTADO).toString();
		String direccion = info.get(DIRECCION).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		int nPlazas = Integer.parseInt(info.get("nPlazas").toString());
		try {
			this.vehiculoService.modificarCoche(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, nPlazas);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	public void modificarMoto(Map<String, Object> info) {
		String id = info.get("id").toString();
		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = Integer.parseInt(info.get(BATERIA).toString());
		String modelo = info.get(MODELO).toString();
		String estado = info.get(ESTADO).toString();
		String direccion = info.get(DIRECCION).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		boolean casco = Boolean.parseBoolean(info.get("casco").toString());
		try {
			this.vehiculoService.modificarMoto(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, casco);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	public void modificarPatinete(Map<String, Object> info) {
		String id = info.get("id").toString();
		String matricula = info.get(MATRICULA).toString();
		String tipo = info.get(TIPO).toString();
		int bateria = Integer.parseInt(info.get(BATERIA).toString());
		String modelo = info.get(MODELO).toString();
		String estado = info.get(ESTADO).toString();
		String direccion = info.get(DIRECCION).toString();
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		String color = info.get("color").toString();
		try {
			this.vehiculoService.modificarPatinete(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, color);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping("/getParametros")
	public Parametros getParametros(@RequestBody Map<String, Object> info) {
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		try {
			return this.adminService.getParametros(emailAdmin,passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	@PutMapping("/actualizarParametros")
	public void actualizarParametros(@RequestBody Map<String, Object> info) {
		double precioViaje = Double.parseDouble(info.get("precioViaje").toString());
		int minimoBateria = Integer.parseInt(info.get("minimoBateria").toString());
		int bateriaViaje = Integer.parseInt(info.get("bateriaViaje").toString());
		int maxVehiculosMantenimiento = Integer.parseInt(info.get("maxVehiculosMantenimiento").toString());
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();

		try {
			this.adminService.actualizarParametros(precioViaje, minimoBateria, bateriaViaje, maxVehiculosMantenimiento, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	@PostMapping("/obtenerFacturacion")
	public double obtenerFacturacion(@RequestBody Map<String, Object> info) {
		String emailAdmin = info.get(EMAILUSER).toString();
		String passwordAdmin = info.get(PASSWORDUSER).toString();
		String emailCliente = info.get("emailCliente").toString();
		String primerDia = info.get("primerDia").toString();
		String ultimoDia = info.get("ultimoDia").toString();
		try {
			return this.adminService.obtenerFacturacion(emailAdmin,passwordAdmin,emailCliente, primerDia, ultimoDia);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
