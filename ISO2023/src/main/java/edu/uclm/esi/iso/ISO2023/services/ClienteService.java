package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

@Service
public class ClienteService {
	@Autowired
	private UserService userService;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	@Autowired
	private AdminService adminService;

	public List<Cliente> listaClientes(String emailAdmin, String passwordAdmin) {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin"))
			return this.clienteDAO.findAll();
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta accion.");
		}
	}

	public void actualizarCliente(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String fechaNacimiento, String carnet, String telefono, String dni, String emailAdmin,
			String passwordAdmin) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin")) {
			Cliente cliente = new Cliente(nombre, apellidos, email, password, activo, intentos, fechaNacimiento, carnet,
					telefono, dni);

			Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
			if (clienteExiste.isPresent()) {
				comprobarSeguridad.restriccionesPassword(cliente);
				comprobarSeguridad.validarEmail(cliente.getEmail());
				comprobarSeguridad.comprobarNumero(cliente.getTelefono());

				if (Boolean.FALSE.equals(comprobarSeguridad.comprobarDni(cliente.getDni())))
					throw new numeroInvalido(
							"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

				if (cliente.getPassword().length() != 60) {
					cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
				}
				cliente.setNombre(nombre);
				cliente.setApellidos(apellidos);
				cliente.setEmail(email);
				cliente.setPassword(password);
				cliente.setActivo(activo);
				cliente.setIntentos(intentos);
				cliente.setFechaNacimiento(fechaNacimiento);
				cliente.setCarnet(carnet);
				cliente.setTelefono(telefono);
				cliente.setDni(dni);
				clienteDAO.save(cliente);
			}
		}
	}

	public void eliminarCliente(String email, String emailAdmin, String passwordAdmin) {
		if (userService.comprobarUsuario(emailAdmin, passwordAdmin).equals("admin")) {
			Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
			if (clienteExiste.isPresent()) {
				clienteDAO.deleteById(email);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
			}
		}
	}
}
