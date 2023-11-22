package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;
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
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
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

	public static final String ERROR_CL = "Ese cliente no existe.";

	public void registrarse(String nombre, String apellidos, String email, String password, String emailAdmin,
			String passwordAdmin, String tipoUsuario) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals("admin")) {
			User usuario = null;
			Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
			Optional<Cliente> clienteExist = this.clienteDAO.findByEmail(email);
			Optional<Mantenimiento> mantExist = this.mantenimientoDAO.findByEmail(email);

			if (tipoUsuario.equals("admin")) {
				usuario = new Administrador(nombre, apellidos, email, password, true, 5);
			} else if (tipoUsuario.equals("mantenimiento")) {
				usuario = new Mantenimiento(nombre, apellidos, email, password, true, 5);
			}

			if (usuario != null) {
				if (!comprobarSeguridad.restriccionesPassword(usuario))
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");

				if (adminExist.isPresent() || clienteExist.isPresent() || mantExist.isPresent()) {
					throw new formatoIncompleto("Error. No puedes usar esos credenciales.");
				} else {
					Token token = new Token();
					token.setUser(usuario);
					comprobarSeguridad.restriccionesPassword(usuario);
					comprobarSeguridad.validarEmail(usuario.getEmail());

					if (usuario.getPassword().length() != 60) {
						usuario.setPassword(comprobarSeguridad.cifrarPassword(usuario.getPassword()));
					}

					if (usuario instanceof Administrador) {
						this.adminDAO.save((Administrador) usuario);
					} else if (usuario instanceof Mantenimiento) {
						this.mantenimientoDAO.save((Mantenimiento) usuario);
					}

					this.tokenDAO.save(token);
				}
			}
		}
	}

	public void actualizarAdmin(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String emailAdmin, String passwordAdmin) throws contraseñaIncorrecta, formatoIncompleto {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals("admin")) {
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
		if (userService.checkUser(emailAdmin, passwordAdmin).equals("admin"))
			adminDAO.deleteById(email);
	}

	public Parametros getParametros(String email, String password) {
		if (userService.checkUser(email, password).equals("admin")) {
			return this.parametrosDAO.findById("655b1a3db7bb7908becebbf4").get();
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes consultar los parametros.");
		}
	}

	public void actualizarParametros(double precioViaje, int minimoBateria, int bateriaViaje,
			int maxVehiculosMantenimiento, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals("admin")) {
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
		double facturacion = 0;
		if (userService.checkUser(emailAdmin, passwordAdmin).equals("admin")) {
			Cliente cliente = this.clienteDAO.findByEmail(emailCliente).get();
			Parametros par = getParametros(emailAdmin, passwordAdmin);
			for (Reserva reserva : this.reservaDAO.findByUsuarioEmailAndFechaBetweenAndEstado(emailCliente, primerDia,
					ultimoDia, "finalizada")) {
				facturacion += this.parametrosDAO.findById("655b1a3db7bb7908becebbf4").get().getPrecioViaje();
			}
		}
		return facturacion;
	}

}
