package controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import entities.Cliente;
import services.UserService;

public class UserController {
	@RestController
	@RequestMapping("users")
	@CrossOrigin("*")
	public class UsersController {
		
		@Autowired
		private UserService userService;
		
		@PostMapping("/register")
		public void registrarse(@RequestBody Map<String, Object> info) {
			String password1 = info.get("password1").toString();
			String passwordd2 = info.get("password2").toString();
			if (!password1.equals(passwordd2))
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
			
			String nombre= info.get("nombre").toString();
			String apellidos = info.get("apellidos").toString();
			String email = info.get("email").toString();
			String ciudad = info.get("ciudad").toString();
			String carnet = info.get("carnet").toString();
			String telefono = info.get("telefono").toString();
			String dni = info.get("dni").toString();
			
			try {
				this.userService.registrarse(nombre, apellidos, email, password1, ciudad, carnet, telefono, dni);
			}catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
			}
			
		}
	}
}
