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
	private AdminDAO adminDAO;

	private SeguridadService comprobarSeguridad = new SeguridadService();

	public List<Cliente> listaClientes(String email, String password) {
		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
		if(!adminExist.isPresent()||!comprobarSeguridad.decodificador(password, adminExist.get().getPassword()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver los vehiculos.");
		return this.clienteDAO.findAll();
	}

	public void actualizarCliente(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String fechaNacimiento, String carnet, String telefono, String dni)
			throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		Cliente cliente = new Cliente(nombre, apellidos, email, password, activo, intentos, fechaNacimiento, carnet,
				telefono, dni);
		if (cliente.getNombre().equals("") || cliente.getApellidos().equals("") || cliente.getEmail().equals("")
				|| cliente.getPassword().equals("") || cliente.getActivo().equals(Boolean.parseBoolean("")) || cliente.getCarnet().equals("")
				|| cliente.getTelefono().equals("") || cliente.getDni().equals(""))
			throw new formatoIncompleto("Introduzca todos los datos");

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
}
