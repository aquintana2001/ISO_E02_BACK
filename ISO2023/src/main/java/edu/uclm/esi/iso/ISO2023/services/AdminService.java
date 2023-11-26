package edu.uclm.esi.iso.ISO2023.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.MantenimientoDAO;
import edu.uclm.esi.iso.ISO2023.dao.ParametrosDAO;
import edu.uclm.esi.iso.ISO2023.dao.ReservaDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Mantenimiento;
import edu.uclm.esi.iso.ISO2023.entities.Parametros;
import edu.uclm.esi.iso.ISO2023.entities.Reserva;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.entities.User;
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
	private MantenimientoDAO mantenimientoDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	@Autowired
	private ParametrosDAO parametrosDAO;
	@Autowired
	private ReservaDAO reservaDAO;

	private static final String ADMIN = "admin";
	private static final String MANTENIMIENTO = "mantenimiento";
	private static final String ID_BBDD = "655b1a3db7bb7908becebbf4";
	public static final String ERROR_CL = "Ese cliente no existe.";

	public void registrar(String nombre, String apellidos, String email, String password, String emailAdmin,
			String passwordAdmin, String tipoUsuario) throws contrasenaIncorrecta, formatoIncompleto {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			User usuario = null;
			switch (tipoUsuario) {
			case ADMIN:
				usuario = new Administrador(nombre, apellidos, email, password, true, 5);
				break;
			case MANTENIMIENTO:
				usuario = new Mantenimiento(nombre, apellidos, email, password, true, 5);
				break;
			default:
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al crear el usuario.");
			}
			seguridadRegistrar(usuario, tipoUsuario);
		}
	}

	public void seguridadRegistrar(User usuario, String tipoUsuario) throws contrasenaIncorrecta, formatoIncompleto {
		if (!comprobarSeguridad.restriccionesPassword(usuario))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");
		if (this.clienteDAO.findByEmail(usuario.getEmail()).isPresent()
				|| this.adminDAO.findByEmail(usuario.getEmail()).isPresent()
				|| this.mantenimientoDAO.findByEmail(usuario.getEmail()).isPresent()) {
			throw new formatoIncompleto("Error. No puedes usar esos credenciales.");
		} else {
			Token token = new Token();
			token.setUser(usuario);
			comprobarSeguridad.restriccionesPassword(usuario);
			comprobarSeguridad.validarEmail(usuario.getEmail());

			if (usuario.getPassword().length() != 60) {
				usuario.setPassword(comprobarSeguridad.cifrarPassword(usuario.getPassword()));
			}

			userService.saveUser(usuario, tipoUsuario);

			this.tokenDAO.save(token);

		}
	}

	public void actualizarAdmin(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
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
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN))
			adminDAO.deleteById(email);
	}

	public Parametros getParametros(String email, String password) {
		Optional<Parametros> par = this.parametrosDAO.findById(ID_BBDD);
		if (userService.checkUser(email, password).equals(ADMIN)) {
			if (par.isPresent())
				return par.get();
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes consultar los parametros.");
		}
		return null;
	}

	public void actualizarParametros(double precioViaje, int minimoBateria, int bateriaViaje,
			int maxVehiculosMantenimiento, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Parametros par = getParametros(emailAdmin, passwordAdmin);
			par.setPrecioViaje(precioViaje);
			par.setMinimoBateria(minimoBateria);
			par.setBateriaViaje(bateriaViaje);
			par.setMaxVehiculosMantenimiento(maxVehiculosMantenimiento);
			this.parametrosDAO.save(par);
		}
	}

	public double obtenerFacturacion(String emailAdmin, String passwordAdmin, String emailCliente, String primerDia,
			String ultimoDia) {
		Optional<Parametros> par = this.parametrosDAO.findById(ID_BBDD);
		double facturacion = 0;
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN) && par.isPresent()) {
			for (Reserva reserva : this.reservaDAO.findByUsuarioEmailAndFechaBetweenAndEstado(emailCliente, primerDia,
					ultimoDia, "finalizada")) {
					facturacion += par.get().getPrecioViaje();
			}
		}
		return facturacion;
	}

}
