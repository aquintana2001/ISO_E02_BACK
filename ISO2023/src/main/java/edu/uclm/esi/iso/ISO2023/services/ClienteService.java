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
	private ClienteDAO clienteDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	@Autowired
	private AdminService adminService;

	public List<Cliente> listaClientes(String emailAdmin, String passwordAdmin) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		return this.clienteDAO.findAll();
	}

	public void actualizarCliente(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String fechaNacimiento, String carnet, String telefono, String dni, String emailAdmin, String passwordAdmin)
			throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		Cliente cliente = new Cliente(nombre, apellidos, email, password, activo, intentos, fechaNacimiento, carnet,
				telefono, dni);
		
		Cliente auxiliar = cliente;
		comprobarSeguridad.restriccionesPassword(auxiliar);
		comprobarSeguridad.validarEmail(cliente.getEmail());
		comprobarSeguridad.comprobarNumero(cliente.getTelefono());

		if (Boolean.FALSE.equals(comprobarSeguridad.comprobarDni(cliente.getDni())))
			throw new numeroInvalido(
					"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

		if (cliente.getPassword().length() != 60) {
			cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
		}

		clienteDAO.save(cliente);
	}
	
	public void eliminarCliente(String email, String emailAdmin, String passwordAdmin) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
		if (clienteExiste.isPresent()) {
			clienteDAO.deleteById(email);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
		}
	}
}
