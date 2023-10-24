package services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import dao.ClienteDAO;
import dao.VehiculoDAO;
import entities.Cliente;
import entities.Vehiculo;

public class AdminService {
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	
	public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }
	
	public List<Vehiculo>listarVehiculos() {
        return vehiculoDAO.findAll();
    }
	
	public void actualizar() {
		
	}
	
}
