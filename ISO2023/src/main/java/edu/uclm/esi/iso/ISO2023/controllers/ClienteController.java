package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.entities.Reserva;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import edu.uclm.esi.iso.ISO2023.services.AdminService;
import edu.uclm.esi.iso.ISO2023.services.ClienteService;
import edu.uclm.esi.iso.ISO2023.services.ReservaService;
import edu.uclm.esi.iso.ISO2023.services.VehiculoService;

@RestController
@RequestMapping("cliente")
@CrossOrigin("*")
public class ClienteController {
	@Autowired
	private VehiculoService vehiculoService;
	@Autowired
	private ReservaService reservaService;
	@Autowired
	private ClienteService clienteService;

	@DeleteMapping("/darDeBaja")
	public void darDeBaja(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		clienteService.darDeBaja(email, password);
	}
	
	@PutMapping("/actualizarDatos")
	public ResponseEntity<String> actualizarDatos(@RequestBody Map<String, Object> info) {
		String nombre = info.get("nombre").toString();
		String email = info.get("email").toString();
		String apellidos = info.get("apellidos").toString();
		String password = info.get("password").toString();
		String fechaNacimiento = info.get("fechaNacimiento").toString();
		String carnet = info.get("carnet").toString();
		String telefono = info.get("telefono").toString();
		String dni = info.get("dni").toString();

		try {
			this.clienteService.actualizarDatos(nombre, apellidos, email, password, fechaNacimiento, carnet, telefono, dni);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Actualizacion realizada con éxito.");
	}
	
	@PostMapping("/vehiculo")
	public List<Vehiculo> listaVehiculo(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		return vehiculoService.listaVehiculo(email, password);
	}
	
	@PostMapping("/reserva")
	public ResponseEntity<String> realizarReserva(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		String idVehiculo = info.get("idVehiculo").toString();
		try {
			this.reservaService.realizarReserva(email,password,idVehiculo);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva realizada con éxito.");
	}
	
	@PutMapping("/cancelarReserva")
	public ResponseEntity<String> cancelarReserva(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		String idReserva = info.get("idReserva").toString();

		try {
			this.reservaService.cancelarReserva(email,password, idReserva);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva cancelada con éxito.");
	}
	
	@PutMapping("/finalizarReserva")
	public ResponseEntity<String> finalizarReserva(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		String idReserva = info.get("idReserva").toString();

		try {
			this.reservaService.finalizarReserva(email,password, idReserva);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva finalizada con éxito.");
	}
	
	@PutMapping("/valorarReserva")
	public void valorarReserva(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		String idReserva = info.get("idReserva").toString();
		double valoracion = Double.parseDouble(info.get("valoracion").toString());
		String comentario = info.get("comentario").toString();
		try {
			this.reservaService.valorarReserva(email,password, idReserva, valoracion, comentario);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
	
	@GetMapping("/listarReservas")
	public List<Reserva> listarReservas(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		try {
			return this.reservaService.listarReservas(email,password);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
