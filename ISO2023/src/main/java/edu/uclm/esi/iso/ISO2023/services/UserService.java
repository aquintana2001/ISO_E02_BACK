package edu.uclm.esi.iso.ISO2023.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.exceptions.*;

@Service
public class UserService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private SeguridadService comprobarSeguridad;

	@Autowired
	private AdminService adminService = new AdminService();
	@Autowired
	private ClienteService clienteService = new ClienteService();

	public void registrarse(String nombre, String apellidos, String email, String password, String fechaNacimiento,
			String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {

		Cliente cliente = new Cliente(nombre, apellidos, email, password, true, 5, fechaNacimiento, carnet, telefono,
				dni);


		Optional<Cliente> userExist = this.clienteDAO.findByEmail(email);
		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);

		if (!comprobarSeguridad.restriccionesPassword(cliente))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");


		if (userExist.isPresent() || adminExist.isPresent())
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");
		userExist = this.clienteDAO.findByDni(dni);
		if (userExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");

		} else {
			Token token = new Token();

			token.setUser(cliente);
			comprobarSeguridad.restriccionesPassword(cliente);
			comprobarSeguridad.validarEmail(cliente.getEmail());
			comprobarSeguridad.comprobarNumero(cliente.getTelefono());


			if (!(comprobarSeguridad.comprobarDni(cliente.getDni())))
				throw new numeroInvalido(
						"El NIF introducido no es un NIF valido. Tiene que contener 8 numeros y un caracter");

			if (cliente.getPassword().length() != 60) {

				cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
			}
			this.clienteDAO.save(cliente);
			this.tokenDAO.save(token);
		}
	}


	public String loginUser(String email, String password) throws formatoIncompleto, numeroInvalido {
		Optional<Administrador> possibleAdmin = this.adminDAO.findByEmail(email);
		Optional<Cliente> possibleCliente = this.clienteDAO.findByEmail(email);
		String errMsg = "Credenciales incorrectos";
		String usuario = null;

		if (Boolean.FALSE.equals(comprobarSeguridad.validarEmail(email)))
			throw new formatoIncompleto(errMsg);

		if (!possibleAdmin.isPresent() && !possibleCliente.isPresent())
			throw new numeroInvalido(errMsg);

		if (possibleAdmin.isPresent()) {

			boolean passwordAdmin = comprobarSeguridad.decodificador(password, possibleAdmin.get().getPassword());

			if (possibleAdmin.get().getIntentos() <= 0)
				throw new numeroInvalido(
						"Este usuario esta bloqueado debido a multiples inicios fallidos de sesion o decision de un administrador. Si necesita ayuda consulte con un administrador de la aplicacion de TIComo");

			if (!passwordAdmin) {
				possibleAdmin.get().setIntentos(possibleAdmin.get().getIntentos() - 1);
				adminDAO.save(possibleAdmin.get());
				throw new formatoIncompleto(errMsg);
			}
			possibleAdmin.get().setIntentos(5);
			adminDAO.save(possibleAdmin.get());
			usuario = "admin";
		}

		if (possibleCliente.isPresent()) {

			boolean passwordCliente = comprobarSeguridad.decodificador(password, possibleCliente.get().getPassword());
			if (possibleCliente.get().getIntentos() <= 0)
				throw new numeroInvalido("Este usuario esta bloqueado debido a multiples inicios fallidos de sesion o decision de un administrador. Si necesita ayuda consulte con un administrador de la aplicacion de TIComo");

			if (!passwordCliente) {
				possibleCliente.get().setIntentos(possibleCliente.get().getIntentos() - 1);
				clienteDAO.save(possibleCliente.get());
				throw new formatoIncompleto(errMsg);
			}

			possibleCliente.get().setIntentos(5);
			clienteDAO.save(possibleCliente.get());
			usuario = "cliente";
		}

		return usuario;
	}

}