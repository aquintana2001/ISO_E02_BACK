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
	
	private static final String EMAILUSER = "emailUser";
	private static final String PASSWORDUSER = "passwordUser";
	private static final String GET_PAR_ERR = "No se han podido capturar los parámetros de la petición, revíselos.";

	@PostMapping("/register")
	public ResponseEntity<byte[]> registrarse(@RequestBody Map<String, Object> info) {

		String password1;
		String password2;
		String nombre;
		String apellidos;
		String email;
		String fechaNacimiento;
		String carnet;
		String telefono;
		String dni;
		byte[] QR ;
		try {
			password1 = info.get("password1").toString();
			password2 = info.get("password2").toString();
			if (!password1.equals(password2))
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");

			nombre = info.get("nombre").toString();
			apellidos = info.get("apellidos").toString();
			email = info.get("email").toString();
			fechaNacimiento = info.get("fechaNacimiento").toString();
			carnet = info.get("carnet").toString();
			telefono = info.get("telefono").toString();
			dni = info.get("dni").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			QR=this.userService.registrarse(nombre, apellidos, email, password1, fechaNacimiento, carnet, telefono, dni);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok(QR);
	}

	@PostMapping("/confirmarRegister")
	public ResponseEntity<String> confirmarRegister(@RequestBody Map<String, Object> info) {
		int mfaKey = (int) info.get("password1");
		String email = info.get("email").toString();

		try {
			this.userService.confirmarRegister(email,mfaKey);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Registro realizado con éxito.");
	}

	
	@PutMapping("/login")
	public String loginUser(@RequestBody Map<String, Object> info) {
		String usuario;
		String email;
		String password;
		int mfaKey;
		try {
			email = info.get("email").toString();
			password = info.get("password").toString();
			mfaKey = (int) info.get("mfaKey");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			usuario = this.userService.loginUser(email, password, mfaKey);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		return usuario;
	}
	
	@PostMapping("/reserva")
	public ResponseEntity<String> realizarReserva(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		String idVehiculo;
		try {
			email = info.get(EMAILUSER).toString();
			password = info.get(PASSWORDUSER).toString();
			idVehiculo = info.get("idVehiculo").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
		try {
			this.reservaService.realizarReserva(email,password,idVehiculo);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva realizada con éxito.");
	}
	
	@PutMapping("/cancelarReserva")
	public ResponseEntity<String> cancelarReserva(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		String idReserva;
		try {
			email = info.get("emailUser").toString();
			password = info.get("passwordUser").toString();
			idReserva = info.get("idReserva").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			this.reservaService.cancelarReserva(email,password, idReserva);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva cancelada con éxito.");
	}
	
	@PutMapping("/finalizarReserva")
	public ResponseEntity<String> finalizarReserva(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		String idReserva;
		try {
			email = info.get("emailUser").toString();
			password = info.get("passwordUser").toString();
			idReserva = info.get("idReserva").toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}

		try {
			this.reservaService.finalizarReserva(email,password, idReserva);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
		return ResponseEntity.ok("Reserva finalizada con éxito.");
	}
	
	@PostMapping("/vehiculo")
	public List<Vehiculo> listaVehiculos(@RequestBody Map<String, Object> info) {
		String email;
		String password;
		try {
			email = info.get(EMAILUSER).toString();
			password = info.get(PASSWORDUSER).toString();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, GET_PAR_ERR);
		}
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
