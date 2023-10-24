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
import eda.service.Administrator;
import eda.service.NonAdminValidationException;
import entities.Administrador;
import entities.Cliente;
import entities.Token;
import entities.Vehiculo;
import exceptions.*;

public class AdminService {
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	
	
	
	public void registrarse(String nombre, String apellidos, String email, String password1) {
		Administrador administrador = new Administrador(nombre, apellidos, email, password1, true, 5);
		
		Optional<Administrador> adminExist = this.adminDAO.findByEmail(email);
		
		if(!administrador.pwdSecure(password1))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña no es segura");
		
		if(adminExist.isPresent()) {
			Optional<Token> tokenExist = this.tokenDAO.findByUser(administrador);
			if (tokenExist.isPresent()) {
				long hora = System.currentTimeMillis();
				if (hora - tokenExist.get().getHoraCreacion() < 1000*60*60*24)
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Debes validar tu cuenta");
				this.tokenDAO.delete(tokenExist.get());
				Token token= new Token();
				token.setUser(administrador);
				this.tokenDAO.save(token);
			}
		}else if(!adminExist.isPresent()) {
			Token token= new Token();
			token.setUser(administrador);
			this.adminDAO.save(administrador);
			this.tokenDAO.save(token);
		}else if(adminExist.isPresent()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esos credenciales no pueden ser usados");
		}
	}
	
	
	
	public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }
	
	public List<Vehiculo>listarVehiculos() {
        return vehiculoDAO.findAll();
    }
	
	public void actualizarAdmin(Administrador admin) throws contraseñaIncorrecta, formatoIncompleto{
		if (admin.getNombre().equals("") || admin.getApellidos().equals("") || admin.getPassword().equals("")
			|| admin.getEmail().equals("") || admin.getActivo().equals(""))
			throw new formatoIncompleto("Rellena todos los campos obligatorios");
		adminDAO.save(admin);
	}
	
	public void actualizarIntentosAdmin(String email, int intentos) throws formatoIncompleto {
		
		Optional<Administrador> admin = adminDAO.findByEmail(email);
		
		if(!admin.isPresent())
			throw new formatoIncompleto("Imposible encontrar al admin");
		
		admin.get().setIntentos(intentos);
		
		adminDAO.save(admin.get());
	}
		
	
}
