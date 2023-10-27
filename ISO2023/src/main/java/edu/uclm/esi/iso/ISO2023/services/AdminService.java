package edu.uclm.esi.iso.ISO2023.services;

import java.io.IOException; 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.TokenDAO;
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Token;
import edu.uclm.esi.iso.ISO2023.exceptions.*;


@Service
public class AdminService {
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	
	private SeguridadService comprobarSeguridad = new SeguridadService();
	
	
	
	public void registrarse(String nombre, String apellidos, String email, String password) throws contraseñaIncorrecta, formatoIncompleto, numeroInvalido {
		Administrador admin = new Administrador(nombre, apellidos, email, password, true, 5);
		
		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
		
		if(!comprobarSeguridad.restriccionesPassword(admin))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");
		
		if(adminExist.isPresent()) {
			throw new formatoIncompleto("Error.No puedes usar esos credenciales.");
		
		}else{
			Token token= new Token();
			token.setUser(admin);
			comprobarSeguridad.restriccionesPassword(admin);
			comprobarSeguridad.validarEmail(admin.getEmail());
			
			if(admin.getPassword().length() != 60) {
				admin.setPassword(comprobarSeguridad.cifrarPassword(admin.getPassword()));
			}
			this.adminDAO.save(admin);
			this.tokenDAO.save(token);
		}
	}
	
	public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }
	
// TDD
//	public List<Vehiculo>listarVehiculos() {
//        return vehiculoDAO.findAll();
//    }
	
	public void actualizarAdmin(String nombre, String apellidos, String email, String password, boolean activo, int intentos) throws contraseñaIncorrecta, formatoIncompleto{
		Optional<Administrador> adminExiste = adminDAO.findByEmail(email);
		if(adminExiste.isPresent()) {
			Administrador admin = adminExiste.get();
			admin.setNombre(nombre);
			admin.setApellidos(apellidos);
			admin.setEmail(email);
			admin.setPassword(password);
			admin.setActivo(activo);
			admin.setIntentos(intentos);
			this.adminDAO.save(admin);
		}else{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
		}
	}
	
	
	public void actualizarCliente(String nombre, String apellidos, String email, String password, boolean activo, int intentos, String fechaNacimiento,
 String carnet, String telefono, String dni) throws contraseñaIncorrecta, formatoIncompleto{
		Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
		if(clienteExiste.isPresent()) {
			Cliente cliente = clienteExiste.get();
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
			this.clienteDAO.save(cliente);
			
		}else{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
		}
	}
		
	public void eliminarAdmin(String email) {
		Optional<Administrador> adminExiste = adminDAO.findByEmail(email);
		if(adminExiste.isPresent()) {
			adminDAO.deleteById(email);
		}else{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
		}
	}
	
	public void eliminarCliente(String email) {
		Optional<Cliente> clienteExiste = clienteDAO.findByEmail(email);
		if(clienteExiste.isPresent()) {
			adminDAO.deleteById(email);
		}else{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese cliente no existe");
		}
	}
	
public void updateAdmnIntentos(String email, int intentos) throws formatoIncompleto {
		
		Optional<Administrador> admin = adminDAO.findByEmail(email);
		
		if(!admin.isPresent())
			throw new formatoIncompleto("Imposible encontrar al admin");
		
		admin.get().setIntentos(intentos);
		
		adminDAO.save(admin.get());
		
	}
	
}
