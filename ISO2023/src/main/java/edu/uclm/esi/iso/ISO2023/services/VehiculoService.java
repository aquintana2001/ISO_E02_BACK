package edu.uclm.esi.iso.ISO2023.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Coche;
import edu.uclm.esi.iso.ISO2023.entities.Moto;
import edu.uclm.esi.iso.ISO2023.entities.Patinete;

@Service
public class VehiculoService {
	@Autowired
	private VehiculoDAO vehiculoDAO;
	
	public void darAltaCoche(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, int nPlazas) {
		Coche coche = new Coche(tipo, matricula, bateria, modelo, estado, direccion, nPlazas);
		if(!vehiculoExist(coche.getId())) {
			this.vehiculoDAO.save(coche);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo ya existe");
		}
	}
	
	public void darAltaMoto(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, boolean casco) {
		Moto moto = new Moto(tipo, matricula, bateria, modelo, estado, direccion, casco);
		if(!vehiculoExist(moto.getId())) {
			this.vehiculoDAO.save(moto);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo ya existe");
		}
	}
	
	public void darAltaPatinete(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, String color) {
		Patinete patinete = new Patinete(tipo, matricula, bateria, modelo, estado, direccion, color);
		if(!vehiculoExist(patinete.getId())) {
			this.vehiculoDAO.save(patinete);
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo ya existe");
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
