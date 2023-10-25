package services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import dao.AdminDAO;
import dao.ClienteDAO;
import dao.TokenDAO;
import dao.VehiculoDAO;
import entities.Administrador;
import entities.Cliente;
import entities.Token;
import entities.User;
import entities.Vehiculo;
import exceptions.*;

public class ClienteService {
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	
	private SeguridadService comprobarSeguridad = new SeguridadService();
	
	public void actualizarCliente(Cliente cliente) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		
		if (cliente.getNombre().equals("") || cliente.getApellidos().equals("")
				|| cliente.getEmail().equals("") || cliente.getPassword().equals("")
				|| cliente.getActivo().equals("") || cliente.getCarnet().equals("")
				|| cliente.getTelefono().equals("") || cliente.getDni().equals("")) 
			throw new formatoIncompleto("Introduzca todos los datos");
		
		Cliente auxiliar= cliente;
		comprobarSeguridad.restriccionesPassword(auxiliar);
		comprobarSeguridad.validarEmail(cliente.getEmail());
		comprobarSeguridad.comprobarNumero(cliente.getTelefono());
		
		if(Boolean.FALSE.equals(comprobarSeguridad.comprobarDni(cliente.getDni())))
			throw new numeroInvalido("El NIF introducido no es un NIF vÃ¡lido. Tiene que contener 8 nÃºmeros y un caracter");
		
		if(cliente.getPassword().length() != 60) {
			cliente.setPassword(comprobarSeguridad.cifrarPassword(cliente.getPassword()));
		}
		
		clienteDAO.save(cliente);
	}
}
