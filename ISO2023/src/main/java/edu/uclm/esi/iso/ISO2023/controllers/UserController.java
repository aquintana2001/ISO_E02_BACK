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

import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import edu.uclm.esi.iso.ISO2023.services.ReservaService;
import edu.uclm.esi.iso.ISO2023.services.UserService;
import edu.uclm.esi.iso.ISO2023.services.VehiculoService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ReservaService reservaService;
	@Autowired
	private VehiculoService vehiculoService;

	@PostMapping("/register")
	public ResponseEntity<String> registrarse(@RequestBody Map<String, Object> info) {
		String password1 = info.get("password1").toString();
		String password2 = info.get("password2").toString();
		if (!password1.equals(password2))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");

		String nombre = info.get("nombre").toString();
		String apellidos = info.get("apellidos").toString();
		String email = info.get("email").toString();
		String fechaNacimiento = info.get("fechaNacimiento").toString();
		String carnet = info.get("carnet").toString();
		String telefono = info.get("telefono").toString();
		String dni = info.get("dni").toString();

		try {
			this.userService.registrarse(nombre, apellidos, email, password1, fechaNacimiento, carnet, telefono, dni);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Registro realizado con éxito.");
	}

	@PutMapping("/login")
	public String loginUser(@RequestBody Map<String, Object> info) {
		String usuario;
		String email = info.get("email").toString();
		String password = info.get("password").toString();
		try {
			usuario = this.userService.loginUser(email, password);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		return usuario;
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
	
	@PostMapping("/vehiculo")
	public List<Vehiculo> listaVehiculos(@RequestBody Map<String, Object> info) {
		String email = info.get("emailUser").toString();
		String password = info.get("passwordUser").toString();
		return vehiculoService.listaVehiculo(email, password);
	}
	
//	@PostMapping("/olvidarContrasena")
//	 public ResponseEntity<String> olvidarContrasena(@RequestBody Map<String, Object> info) {
//		String email = info.get("email").toString();
//        try {
//            userService.olvidarContrasena(email);
//            return ResponseEntity.ok("Correo de restablecimiento enviado con éxito.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error al enviar el correo de restablecimiento: " + e.getMessage());
//        }
//    }
//	@PostMapping("/restablecerContrasena")
//	 public ResponseEntity<String> restablecerContrasena(@RequestBody Map<String, Object> info) {
//		
//		String token = info.get("token").toString();
//		String pwd = info.get("password").toString();
//	
//      try {
//   	   userService.restablecerContrasena(token, pwd);
//	        return ResponseEntity.ok("Contraseña restablecida con éxito");
//      } catch (Exception e) {
//          return ResponseEntity.badRequest().body("Error al enviar el correo de restablecimiento: " + e.getMessage());
//      }
//  }

}
