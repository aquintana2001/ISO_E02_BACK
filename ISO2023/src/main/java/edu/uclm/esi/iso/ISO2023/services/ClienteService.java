package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

@Service
public class ClienteService {
	@Autowired
	private UserService userService;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;
	
	private static final String ADMIN = "admin";

	public List<Cliente> listaClientes(String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN))
			return this.clienteDAO.findAll();
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta accion.");
		}
	}

	// METODO PARA EL ADMIN
	public void actualizarCliente(String nombre, String apellidos, String email, String password, boolean activo,
			int intentos, String fechaNacimiento, String carnet, String telefono, String dni, String emailAdmin,
			String passwordAdmin) throws contrasenaIncorrecta, numeroInvalido {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Cliente cliente = new Cliente(nombre, apellidos, email, password, activo, intentos, fechaNacimiento, carnet,
					telefono, dni, "");

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
				cliente.setsecretKey(clienteExiste.get().getsecretKey());
				clienteDAO.save(cliente);
			}
		}
	}

	public void eliminarCliente(String email, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
			if (clienteExiste.isPresent()) {
				clienteExiste.get().setActivo(false);
				clienteDAO.save(clienteExiste.get());
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
			}
		}
	}

	public void darDeBaja(String email, String password) {
		if (userService.checkUser(email, password).equals("cliente")) {
			clienteDAO.deleteById(email);
			tokenDAO.deleteById(email);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error al eliminar la cuenta");
		}

	}

	public Cliente getDatos(String email, String password) {
		Optional<Cliente> cliente = this.clienteDAO.findByEmail(email);
		if(cliente.isPresent() && comprobarSeguridad.decodificador(password, cliente.get().getPassword())){
			return cliente.get();	
		}
		return null;
	}
	
	// METODO PARA EL CLIENTE
	public void actualizarDatos(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contrasenaIncorrecta,  numeroInvalido{
		Optional<Cliente> cliente = this.clienteDAO.findByEmail(email);
		if (userService.checkUser(email, password).equals("cliente") && cliente.isPresent()) {

			comprobarSeguridad.restriccionesPassword(cliente.get());
			comprobarSeguridad.validarEmail(cliente.get().getEmail());
			comprobarSeguridad.comprobarNumero(cliente.get().getTelefono());

			if (Boolean.FALSE.equals(comprobarSeguridad.comprobarDni(cliente.get().getDni())))
				throw new numeroInvalido(
						"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

			if (cliente.get().getPassword().length() != 60) {
				cliente.get().setPassword(comprobarSeguridad.cifrarPassword(cliente.get().getPassword()));
			}
			cliente.get().setNombre(nombre);
			cliente.get().setApellidos(apellidos);
			cliente.get().setPassword(password);
			cliente.get().setFechaNacimiento(fechaNacimiento);
			cliente.get().setCarnet(carnet);
			cliente.get().setTelefono(telefono);
			cliente.get().setDni(dni);
			clienteDAO.save(cliente.get());
		}

	}

}