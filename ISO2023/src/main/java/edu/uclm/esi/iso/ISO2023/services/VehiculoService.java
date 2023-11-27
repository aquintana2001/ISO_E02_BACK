package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import edu.uclm.esi.iso.ISO2023.dao.ParametrosDAO;
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Coche;
import edu.uclm.esi.iso.ISO2023.entities.Moto;
import edu.uclm.esi.iso.ISO2023.entities.Parametros;
import edu.uclm.esi.iso.ISO2023.entities.Patinete;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import java.util.Collections;

@Service
public class VehiculoService {
	@Autowired
	private UserService userService;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	@Autowired
	private ParametrosDAO parametrosDAO;

	public static final String ERROR_VE = "Ese vehiculo ya existe";
	public static final String ID_BBDD = "655b1a3db7bb7908becebbf4";
	public static final String ADMIN = "admin";

	public List<Vehiculo> listaVehiculo(String email, String password) {
		Optional<Parametros> parametros = parametrosDAO.findById(ID_BBDD);
		if (parametros.isPresent()) {
			if (userService.checkUser(email, password).equals(ADMIN)) {
				return this.vehiculoDAO.findAll();
			} else if (userService.checkUser(email, password).equals("cliente")) {
				return this.vehiculoDAO.findByBateriaGreaterThanAndEstadoEquals(parametros.get().getMinimoBateria(),
						"disponible");
			} else if (userService.checkUser(email, password).equals("mantenimiento")) {
				return this.vehiculoDAO.findByBateriaLessThanAndEstadoEquals(parametros.get().getMinimoBateria(),
						"descargado");
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"No tienes permisos para realizar esta accion.");
			}
		}
		return Collections.emptyList();
	}

	public void darAltaCoche(String matricula, String tipo, int bateria, String modelo, String estado, String direccion,
			int nPlazas, String emailAdmin, String passwordAdmin){
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Coche coche = new Coche(tipo, matricula, bateria, modelo, estado, direccion, nPlazas);

			Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);

			if (!possibleVehiculo.isPresent()) {
				this.vehiculoDAO.save(coche);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
			}
		}
	}

	public void darAltaMoto(String matricula, String tipo, int bateria, String modelo, String estado, String direccion,
			boolean casco, String emailAdmin, String passwordAdmin)  {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Moto moto = new Moto(tipo, matricula, bateria, modelo, estado, direccion, casco);
			Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);

			if (!possibleVehiculo.isPresent()) {
				this.vehiculoDAO.save(moto);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
			}
		}
	}

	public void darAltaPatinete(String matricula, String tipo, int bateria, String modelo, String estado,
			String direccion, String color, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Patinete patinete = new Patinete(tipo, matricula, bateria, modelo, estado, direccion, color);
			Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);

			if (!possibleVehiculo.isPresent()) {
				this.vehiculoDAO.save(patinete);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
			}
		}
	}

	public void darBajaVehiculo(String id, String emailAdmin, String passwordAdmin) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			if (vehiculoExist(id)) {
				vehiculoDAO.deleteById(id);
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo no existe");
			}
		}
	}

	public boolean vehiculoExist(String id) {

		return this.vehiculoDAO.findById(id).isPresent();

	}

	public void modificarCoche(String id, String matricula, String tipo, int bateria, String modelo, String estado,
			String direccion, String emailAdmin, String passwordAdmin, int nPlazas) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Optional<Vehiculo> vehiculoExiste = vehiculoDAO.findById(id);
			if (vehiculoExiste.isPresent()) {
				Vehiculo vehiculo = vehiculoExiste.get();
				Coche coche = (Coche) vehiculo;
				coche.setMatricula(matricula);
				coche.setTipo(tipo);
				coche.setBateria(bateria);
				coche.setModelo(modelo);
				coche.setEstado(estado);
				coche.setDireccion(direccion);
				coche.setnPlaza(nPlazas);
				this.vehiculoDAO.save(coche);
			}
		}
	}

	public void modificarMoto(String id, String matricula, String tipo, int bateria, String modelo, String estado,
			String direccion, String emailAdmin, String passwordAdmin, boolean casco) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Optional<Vehiculo> vehiculoExiste = vehiculoDAO.findById(id);
			if (vehiculoExiste.isPresent()) {
				Vehiculo vehiculo = vehiculoExiste.get();
				Moto moto = (Moto) vehiculo;
				moto.setMatricula(matricula);
				moto.setTipo(tipo);
				moto.setBateria(bateria);
				moto.setModelo(modelo);
				moto.setEstado(estado);
				moto.setDireccion(direccion);
				moto.setCasco(casco);
				this.vehiculoDAO.save(moto);
			}
		}
	}

	public void modificarPatinete(String id, String matricula, String tipo, int bateria, String modelo, String estado,
			String direccion, String emailAdmin, String passwordAdmin, String color) {
		if (userService.checkUser(emailAdmin, passwordAdmin).equals(ADMIN)) {
			Optional<Vehiculo> vehiculoExiste = vehiculoDAO.findById(id);
			if (vehiculoExiste.isPresent()) {
				Vehiculo vehiculo = vehiculoExiste.get();
				Patinete patinete = (Patinete) vehiculo;
				patinete.setMatricula(matricula);
				patinete.setTipo(tipo);
				patinete.setBateria(bateria);
				patinete.setModelo(modelo);
				patinete.setEstado(estado);
				patinete.setDireccion(direccion);
				patinete.setColor(color);
				this.vehiculoDAO.save(patinete);
			}
		}
	}
}