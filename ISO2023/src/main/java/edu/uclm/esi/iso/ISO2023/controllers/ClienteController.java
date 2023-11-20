package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
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
		try {
			this.reservaService.valorarReserva(email,password, idReserva, valoracion);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}
