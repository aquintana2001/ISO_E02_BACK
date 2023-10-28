package edu.uclm.esi.iso.ISO2023.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Coche;
import edu.uclm.esi.iso.ISO2023.entities.Moto;
import edu.uclm.esi.iso.ISO2023.entities.Patinete;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;

@Service
public class VehiculoService {
	@Autowired
	private VehiculoDAO vehiculoDAO;
	

	public static final String ERROR_VE = "Ese vehiculo ya existe";

	public List <Vehiculo> listaVehiculo() {
		return this.vehiculoDAO.findAll();
	}
	
	public void darAltaCoche(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, int nPlazas) {
		Coche coche = new Coche(tipo, matricula, bateria, modelo, estado, direccion, nPlazas);
		if(!vehiculoExist(coche.getId())) {
			this.vehiculoDAO.save(coche);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
		}
	}
	
	public void darAltaMoto(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, boolean casco) {
		Moto moto = new Moto(tipo, matricula, bateria, modelo, estado, direccion, casco);
		if(!vehiculoExist(moto.getId())) {
			this.vehiculoDAO.save(moto);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
		}
	}
	
	public void darAltaPatinete(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, String color) {
		Patinete patinete = new Patinete(tipo, matricula, bateria, modelo, estado, direccion, color);
		if(!vehiculoExist(patinete.getId())) {
			this.vehiculoDAO.save(patinete);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
		}
	}
	
	public void darBajaVehiculo(String id) {
		if(vehiculoExist(id)) {
			vehiculoDAO.deleteById(id);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo no existe");
		}
	}
	
	public boolean vehiculoExist(String id) {
		boolean existe = this.vehiculoDAO.findById(id).isPresent();
		return existe;
	}
}
