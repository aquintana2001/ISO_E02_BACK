package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import edu.uclm.esi.iso.ISO2023.dao.AdminDAO;
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Administrador;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Coche;
import edu.uclm.esi.iso.ISO2023.entities.Moto;
import edu.uclm.esi.iso.ISO2023.entities.Patinete;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;
import edu.uclm.esi.iso.ISO2023.exceptions.numeroInvalido;
 
@Service
public class VehiculoService {
	@Autowired
	private VehiculoDAO vehiculoDAO;	
	@Autowired
	private AdminService adminService;
	@Autowired
	private SeguridadService comprobarSeguridad;

	public static final String ERROR_VE = "Ese vehiculo ya existe";

	public List<Vehiculo> listaVehiculo(String emailAdmin, String passwordAdmin) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		return this.vehiculoDAO.findAll();
	}

	public void darAltaCoche(String matricula, String tipo, int bateria, String modelo, String estado, String direccion,
            int nPlazas, String emailAdmin, String passwordAdmin) throws numeroInvalido {
        adminService.comprobarAdmin(emailAdmin, passwordAdmin);
        Coche coche = new Coche(tipo, matricula, bateria, modelo, estado, direccion, nPlazas);

        Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);

        if (!possibleVehiculo.isPresent()) {
            this.vehiculoDAO.save(coche);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
        }
    }

	public void darAltaMoto(String matricula, String tipo, int bateria, String modelo, String estado, String direccion,
			boolean casco, String emailAdmin, String passwordAdmin) throws numeroInvalido{
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		Moto moto = new Moto(tipo, matricula, bateria, modelo, estado, direccion, casco);
		Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);

        if (!possibleVehiculo.isPresent()) {
            this.vehiculoDAO.save(moto);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
        }
	}

	public void darAltaPatinete(String matricula, String tipo, int bateria, String modelo, String estado,
			String direccion, String color, String emailAdmin, String passwordAdmin) throws numeroInvalido {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		Patinete patinete = new Patinete(tipo, matricula, bateria, modelo, estado, direccion, color);
		Optional<Vehiculo> possibleVehiculo = this.vehiculoDAO.findByMatricula(matricula);
		
        if (!possibleVehiculo.isPresent()) {
            this.vehiculoDAO.save(patinete);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_VE);
        }
	}

	public void darBajaVehiculo(String id, String emailAdmin, String passwordAdmin) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
		if (vehiculoExist(id)) {
			vehiculoDAO.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese vehiculo no existe");
		}
	}

	public boolean vehiculoExist(String id) {

		return this.vehiculoDAO.findById(id).isPresent();

	}
	
	public void modificarCoche(String id,String matricula,String tipo,int bateria,String modelo,String estado,String direccion,String emailAdmin,String passwordAdmin, int nPlazas) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
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
	
	public void modificarMoto(String id,String matricula,String tipo,int bateria,String modelo,String estado,String direccion,String emailAdmin,String passwordAdmin, boolean casco) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
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
	
	public void modificarPatinete(String id,String matricula,String tipo,int bateria,String modelo,String estado,String direccion,String emailAdmin,String passwordAdmin, String color) {
		adminService.comprobarAdmin(emailAdmin, passwordAdmin);
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
