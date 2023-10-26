package edu.uclm.esi.iso.ISO2023.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.services.ClienteService;
import edu.uclm.esi.iso.ISO2023.services.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private UserService userService;
	
	@GetMapping("/cliente")
	public List<Cliente> listaCliente(){
		return clienteService.listaClientes();
	}

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
}
