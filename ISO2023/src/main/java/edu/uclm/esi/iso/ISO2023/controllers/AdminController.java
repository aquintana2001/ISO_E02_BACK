package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.*; 

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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
	public static final String GET_PAR_ERR = "No se han podido capturar los parámetros de la petición, revíselos.";

	@PostMapping("/cliente")
	public List<Cliente> listaCliente(@RequestBody Map<String, Object> info) {
		String emailAdmin;
		String passwordAdmin;
		try {
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		return clienteService.listaClientes(emailAdmin, passwordAdmin);
	}

	@PostMapping("/vehiculo")
	public List<Vehiculo> listaVehiculo(@RequestBody Map<String, Object> info) {
		String emailAdmin;
		String passwordAdmin;
		try {
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		return vehiculoService.listaVehiculo(emailAdmin, passwordAdmin);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registrar(@RequestBody Map<String, Object> info) {
		String password1;
		String nombre;
		String apellidos;
		String email;
		String emailAdmin;
		String passwordAdmin;
		String tipoUsuario;
		try {
			password1 = info.get("password1").toString();
			String password2 = info.get("password2").toString();

			if (!password1.equals(password2))
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");

			nombre = info.get(NOMBRE).toString();
			apellidos = info.get(APELLIDOS).toString();
			email = info.get(EMAIL).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			tipoUsuario = info.get("tipoUsuario").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			if (tipoUsuario.equals("admin"))
				this.adminService.registrar(nombre, apellidos, email, password1, emailAdmin, passwordAdmin,
						tipoUsuario);
			if (tipoUsuario.equals("mantenimiento"))
				this.adminService.registrar(nombre, apellidos, email, password1, emailAdmin, passwordAdmin,
						tipoUsuario);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Registro realizado con éxito.");
	}

	@PutMapping("/actualizarCliente")
	public ResponseEntity<String> actualizarCliente(@RequestBody Map<String, Object> info) {
		String nombre;
		String apellidos;
		String email;
		String password;
		boolean activo;
		int intentos;
		String fechaNacimiento;
		String carnet;
		String telefono;
		String dni;
		String emailAdmin;
		String passwordAdmin;
		try {
			nombre = info.get(NOMBRE).toString();
			apellidos = info.get(APELLIDOS).toString();
			email = info.get(EMAIL).toString();
			password = info.get(PASSWORD).toString();
			activo = Boolean.parseBoolean(info.get("activo").toString());
			intentos = Integer.parseInt(info.get("intentos").toString());
			fechaNacimiento = info.get("fechaNacimiento").toString();
			carnet = info.get("carnet").toString();
			telefono = info.get("telefono").toString();
			dni = info.get("dni").toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

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
		String email;
		String emailAdmin;
		String passwordAdmin;
		try {
			email = info.get(EMAIL).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.adminService.eliminarAdmin(email, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Administrador eliminado con éxito.");
	}

	@DeleteMapping("/eliminarCliente")
	public ResponseEntity<String> eliminarCliente(@RequestBody Map<String, Object> info) {
		String email;
		String emailAdmin;
		String passwordAdmin;
		try {
			email = info.get(EMAIL).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.clienteService.eliminarCliente(email, emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Cliente eliminado con éxito.");
	}

	@PostMapping("/darAltaVehiculo")
	public ResponseEntity<String> darAltaVehiculo(@RequestBody Map<String, Object> info) throws numeroInvalido {

		String tipoVehiculo;
		try {
			tipoVehiculo = (String) info.get(TIPO);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

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
		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		int nPlazas;
		String emailAdmin;
		String passwordAdmin;
		try {
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = 100;
			modelo = info.get(MODELO).toString();
			estado = DISPONIBLE;
			direccion = info.get(DIRECCION).toString();
			nPlazas = Integer.parseInt(info.get("nPlazas").toString());
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		this.vehiculoService.darAltaCoche(matricula, tipo, bateria, modelo, estado, direccion, nPlazas, emailAdmin,
				passwordAdmin);

	}

	private void darAltaMoto(Map<String, Object> info) throws numeroInvalido {

		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		boolean casco;
		String emailAdmin;
		String passwordAdmin;
		try {
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = 100;
			modelo = info.get(MODELO).toString();
			estado = DISPONIBLE;
			direccion = info.get(DIRECCION).toString();
			casco = Boolean.parseBoolean(info.get("casco").toString());
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		this.vehiculoService.darAltaMoto(matricula, tipo, bateria, modelo, estado, direccion, casco, emailAdmin,
				passwordAdmin);
	}

	private void darAltaPatinete(Map<String, Object> info) throws numeroInvalido {

		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		String color;
		String emailAdmin;
		String passwordAdmin;
		try {
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = 100;
			modelo = info.get(MODELO).toString();
			estado = DISPONIBLE;
			direccion = info.get(DIRECCION).toString();
			color = info.get("color").toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		this.vehiculoService.darAltaPatinete(matricula, tipo, bateria, modelo, estado, direccion, color, emailAdmin,
				passwordAdmin);
	}

	@DeleteMapping("/darBajaVehiculo")
	public ResponseEntity<String> darBajaVehiculo(@RequestBody Map<String, Object> info) {
		String emailAdmin;
		String passwordAdmin;
		String id;
		try {
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			id = info.get("id").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		vehiculoService.darBajaVehiculo(id, emailAdmin, passwordAdmin);
		return ResponseEntity.ok("Vehículo dado de baja con éxito.");
	}

	@PutMapping("/actualizarVehiculo")
	public ResponseEntity<String> actualizarVehiculo(@RequestBody Map<String, Object> info) {
		String tipoVehiculo;
		try {
			tipoVehiculo = (String) info.get(TIPO);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

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
		String id;
		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		String emailAdmin;
		String passwordAdmin;
		int nPlazas;
		try {
			id = info.get("id").toString();
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = Integer.parseInt(info.get(BATERIA).toString());
			modelo = info.get(MODELO).toString();
			estado = info.get(ESTADO).toString();
			direccion = info.get(DIRECCION).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			nPlazas = Integer.parseInt(info.get("nPlazas").toString());
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.vehiculoService.modificarCoche(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, nPlazas);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	public void modificarMoto(Map<String, Object> info) {
		String id;
		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		String emailAdmin;
		String passwordAdmin;
		boolean casco;
		try {
			id = info.get("id").toString();
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = Integer.parseInt(info.get(BATERIA).toString());
			modelo = info.get(MODELO).toString();
			estado = info.get(ESTADO).toString();
			direccion = info.get(DIRECCION).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			casco = Boolean.parseBoolean(info.get("casco").toString());
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.vehiculoService.modificarMoto(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, casco);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	public void modificarPatinete(Map<String, Object> info) {
		String id;
		String matricula;
		String tipo;
		int bateria;
		String modelo;
		String estado;
		String direccion;
		String emailAdmin;
		String passwordAdmin;
		String color;
		try {
			id = info.get("id").toString();
			matricula = info.get(MATRICULA).toString();
			tipo = info.get(TIPO).toString();
			bateria = Integer.parseInt(info.get(BATERIA).toString());
			modelo = info.get(MODELO).toString();
			estado = info.get(ESTADO).toString();
			direccion = info.get(DIRECCION).toString();
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			color = info.get("color").toString();
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.vehiculoService.modificarPatinete(id, matricula, tipo, bateria, modelo, estado, direccion, emailAdmin,
					passwordAdmin, color);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping("/getParametros")
	public Parametros getParametros(@RequestBody Map<String, Object> info) {
		String emailAdmin;
		String passwordAdmin;
		try {
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			return this.adminService.getParametros(emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PutMapping("/actualizarParametros")
	public void actualizarParametros(@RequestBody Map<String, Object> info) {
		double precioViaje;
		int minimoBateria;
		int bateriaViaje;
		int maxVehiculosMantenimiento;
		String emailAdmin;
		String passwordAdmin;
		try {
			precioViaje = Double.parseDouble(info.get("precioViaje").toString());
			minimoBateria = Integer.parseInt(info.get("minimoBateria").toString());
			bateriaViaje = Integer.parseInt(info.get("bateriaViaje").toString());
			maxVehiculosMantenimiento = Integer.parseInt(info.get("maxVehiculosMantenimiento").toString());
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			this.adminService.actualizarParametros(precioViaje, minimoBateria, bateriaViaje, maxVehiculosMantenimiento,
					emailAdmin, passwordAdmin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping("/obtenerFacturacion")
	public double obtenerFacturacion(@RequestBody Map<String, Object> info) {
		String emailAdmin;
		String passwordAdmin;
		String emailCliente;
		String primerDia;
		String ultimoDia;
		try {
			emailAdmin = info.get(EMAILUSER).toString();
			passwordAdmin = info.get(PASSWORDUSER).toString();
			emailCliente = info.get("emailCliente").toString();
			primerDia = info.get("primerDia").toString();
			ultimoDia = info.get("ultimoDia").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			return this.adminService.obtenerFacturacion(emailAdmin, passwordAdmin, emailCliente, primerDia, ultimoDia);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
