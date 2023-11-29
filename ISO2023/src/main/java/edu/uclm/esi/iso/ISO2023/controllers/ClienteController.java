package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Reserva;
import edu.uclm.esi.iso.ISO2023.services.ClienteService;
import edu.uclm.esi.iso.ISO2023.services.ReservaService;

@RestController
@RequestMapping("cliente")
@CrossOrigin("*")
public class ClienteController {
	@Autowired
	private ReservaService reservaService;
	@Autowired
	private ClienteService clienteService;
	
	private static final String GET_PAR_ERR = "No se han podido capturar los parametros de la peticion, reviselos.";
	private static final String EMAILUSER = "emailUser";
	private static final String PASSWORDUSER = "passwordUser";

	@DeleteMapping("/darDeBaja")
	public void darDeBaja(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		try {
			email = info.get(EMAILUSER).toString();
			password = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		clienteService.darDeBaja(email, password);
	}
	
	@PostMapping("/getDatos")
	public Cliente getDatos(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		try {
			email = info.get(EMAILUSER).toString();
			password = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			return this.clienteService.getDatos(email, password);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	
	@PutMapping("/actualizarDatos")
	public ResponseEntity<String> actualizarDatos(@RequestBody Map<String, Object> info) {
		String nombre;
		String email;
		String apellidos;
		String password;
		String fechaNacimiento;
		String carnet;
		String telefono;
		String dni;
		try {
			nombre = info.get("nombre").toString();
			email = info.get("email").toString();
			apellidos = info.get("apellidos").toString();
			password = info.get("password").toString();
			fechaNacimiento = info.get("fechaNacimiento").toString();
			carnet = info.get("carnet").toString();
			telefono = info.get("telefono").toString();
			dni = info.get("dni").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			this.clienteService.actualizarDatos(nombre, apellidos, email, password, fechaNacimiento, carnet, telefono, dni);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Actualizacion realizada con éxito.");
	}
	
	
	
	@PutMapping("/valorarReserva")
	public void valorarReserva(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		String idReserva;
		double valoracion;
		String comentario;
		try {
			email = info.get(EMAILUSER).toString();
			password = info.get(PASSWORDUSER).toString();
			idReserva = info.get("idReserva").toString();
			valoracion = Double.parseDouble(info.get("valoracion").toString());
			comentario = info.get("comentario").toString();
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.reservaService.valorarReserva(email,password, idReserva, valoracion, comentario);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
}
