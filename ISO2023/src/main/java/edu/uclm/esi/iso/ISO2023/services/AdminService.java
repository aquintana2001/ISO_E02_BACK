package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.ParametrosDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Parametros;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

@Service
public class AdminService {
	@Autowired
	private UserService userService;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	@Autowired
	private ParametrosDAO parametrosDAO;

	public static final String ERROR_CL = "Ese cliente no existe.";

	public void registrarse(String nombre, String apellidos, String email, String password, String emailAdmin,
			String passwordAdmin) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin")) {
			Administrador admin = new Administrador(nombre, apellidos, email, password, true, 5);

			Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
			Optional<Cliente> clienteExist = this.clienteDAO.findByEmail(email);

			if (!comprobarSeguridad.restriccionesPassword(admin))
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");

			if (adminExist.isPresent() || clienteExist.isPresent()) {
				throw new formatoIncompleto("Error.No puedes usar esos credenciales.");

			} else {
				Token token = new Token();
				token.setUser(admin);
				comprobarSeguridad.restriccionesPassword(admin);
				comprobarSeguridad.validarEmail(admin.getEmail());

				if (admin.getPassword().length() != 60) {
					admin.setPassword(comprobarSeguridad.cifrarPassword(admin.getPassword()));
				}
				this.adminDAO.save(admin);
				this.tokenDAO.save(token);
			}
		}
	}

	public void actualizarAdmin(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String emailAdmin, String passwordAdmin) throws contraseñaIncorrecta, formatoIncompleto {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin")) {
			Optional<Administrador> adminExiste = adminDAO.findByEmail(email);
			if (adminExiste.isPresent()) {
				Administrador admin = adminExiste.get();
				admin.setNombre(nombre);
				admin.setApellidos(apellidos);
				admin.setEmail(email);
				admin.setPassword(password);
				admin.setActivo(activo);
				admin.setIntentos(intentos);
				this.adminDAO.save(admin);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_CL);
			}
		}
	}

	public void eliminarAdmin(String email, String emailAdmin, String passwordAdmin) {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin"))
			adminDAO.deleteById(email);
	}

	public Parametros getParametros(String email, String password) {
		if (userService.comprobarUsuario(email, password).equals("admin")) {
			return this.parametrosDAO.findById("655b1a3db7bb7908becebbf4").get();
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes consultar los parametros.");
		}
	}

	public void actualizarParametros(double precioViaje, int minimoBateria, int bateriaViaje,
			int maxVehiculosMantenimiento, String emailAdmin, String passwordAdmin) {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin")) {
			Parametros par = getParametros(emailAdmin,passwordAdmin);
			par.setPrecioViaje(precioViaje);
			par.setMinimoBateria(minimoBateria);
			par.setBateriaViaje(bateriaViaje);
			par.setMaxVehiculosMantenimiento(maxVehiculosMantenimiento);
			this.parametrosDAO.save(par);
		}
	}

}
