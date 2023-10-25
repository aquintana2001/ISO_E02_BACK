package edu.uclm.esi.iso.ISO2023.services;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Coche;
import edu.uclm.esi.iso.ISO2023.entities.Moto;
import edu.uclm.esi.iso.ISO2023.entities.Patinete;

@Service
public class VehiculoService {
	private VehiculoDAO vehiculoDAO;
	
	public void darAltaCoche(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, int nPlazas) {
		Coche coche = new Coche(tipo, matricula, bateria, modelo, estado, direccion, nPlazas);
		if(vehiculoExist(coche.getId())) {
			this.vehiculoDAO.save(coche);
		}
	}
	
	public void darAltaMoto(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, boolean casco) {
		Moto moto = new Moto(tipo, matricula, bateria, modelo, estado, direccion, casco);
		if(vehiculoExist(moto.getId())) {
			this.vehiculoDAO.save(moto);
		}
	}
	
	public void darAltaPatinete(String matricula, String tipo, int bateria, String modelo, String estado, String direccion, String color) {
		Patinete patinete = new Patinete(tipo, matricula, bateria, modelo, estado, direccion, color);
		if(vehiculoExist(patinete.getId())) {
			this.vehiculoDAO.save(patinete);
		}
	}
	
	public void darBajaVehiculo(int id) {
		if(vehiculoExist(id)) {
			vehiculoDAO.deleteById(id);
		}
	}
	
	public boolean vehiculoExist(int id) {
		boolean existe = this.vehiculoDAO.findById(id).isPresent();
		if(existe) {
			return existe;
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe ese vehiculo");
		}
	}
}
