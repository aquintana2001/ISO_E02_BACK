package edu.uclm.esi.iso.ISO2023.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.iso.ISO2023.dao.ClienteDAO;
import edu.uclm.esi.iso.ISO2023.dao.MantenimientoDAO;
import edu.uclm.esi.iso.ISO2023.dao.ParametrosDAO;
import edu.uclm.esi.iso.ISO2023.dao.ReservaDAO;
import edu.uclm.esi.iso.ISO2023.dao.VehiculoDAO;
import edu.uclm.esi.iso.ISO2023.entities.Cliente;
import edu.uclm.esi.iso.ISO2023.entities.Mantenimiento;
import edu.uclm.esi.iso.ISO2023.entities.Parametros;
import edu.uclm.esi.iso.ISO2023.entities.Reserva;
import edu.uclm.esi.iso.ISO2023.entities.Vehiculo;

@Service
public class ReservaService {
	@Autowired
	private UserService userService;
	@Autowired
	private ReservaDAO reservaDAO;
	@Autowired
	private VehiculoDAO vehiculoDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private MantenimientoDAO mantenimientoDAO;
	@Autowired
	private ParametrosDAO parametrosDAO;

	public void realizarReserva(String email, String password, String idVehiculo) {

		Optional<Cliente> cli = this.clienteDAO.findByEmail(email);
		Optional<Mantenimiento> mant = this.mantenimientoDAO.findByEmail(email);
		Vehiculo vehiculo = this.vehiculoDAO.findById(idVehiculo).get();

		if (userService.comprobarUsuario(email, password).equals("cliente")) {
			if (vehiculo.getEstado().equals("disponible")) {
				if (this.reservaDAO.findByUsuarioEmailAndVehiculoEstado(email, "reservado").isEmpty()) {
					Cliente cliente = cli.get();
					comprobarCarnet(cliente, vehiculo);
					Reserva reserva = new Reserva(vehiculo, cliente, 0.0);
					vehiculo.setEstado("reservado");
					reserva.setEstado("activa");
					this.vehiculoDAO.save(vehiculo);
					this.reservaDAO.save(reserva);
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ya tienes una reserva activa.");
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El vehiculo ya está reservado.");
			}

		} else if (userService.comprobarUsuario(email, password).equals("mantenimiento")) {
			if (vehiculo.getEstado().equals("descargado")) {
				if (this.mantenimientoDAO.findByEmail(email).get().getReservasActivas() < this.parametrosDAO.findById("655b1a3db7bb7908becebbf4").get().getMaxVehiculosMantenimiento()) {
					Mantenimiento mantenimiento = mant.get();
					Reserva reserva = new Reserva(vehiculo, mantenimiento, 0.0);
					vehiculo.setEstado("en mantenimiento");
					reserva.setEstado("activa");
					mantenimiento.setReservasActivas(mantenimiento.getReservasActivas() + 1);
					this.vehiculoDAO.save(vehiculo);
					this.reservaDAO.save(reserva);
					this.mantenimientoDAO.save(mantenimiento);
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No puedes reservar mas vehiculos..");
				}
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El vehiculo no necesita mantenimiento.");
			}
		}
	}
	
	public void comprobarCarnet(Cliente cliente, Vehiculo vehiculo) {
		String carnet;
		if(vehiculo.getTipo().equals("coche")&&!cliente.getCarnet().equals("B")) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Necesitas sacarte el carnet B.");
		}else if(vehiculo.getTipo().equals("moto")&&!(cliente.getCarnet().equals("A")||cliente.getCarnet().equals("B"))){
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Necesitas sacarte el carnet A o B.");
		}
	}

	public void cancelarReserva(String email, String password, String idReserva) {
		Reserva reserva = this.reservaDAO.findById(idReserva).get();
		if ((userService.comprobarUsuario(email, password).equals("cliente")
				|| userService.comprobarUsuario(email, password).equals("mantenimiento"))
				&& reserva.getEstado().equals("activa") && reserva.getUsuario().getEmail().equals(email)) {
			reserva.setEstado("cancelada");
			reserva.getVehiculo().setEstado("disponible");
			if(userService.comprobarUsuario(email, password).equals("mantenimiento")) {
				Mantenimiento mant = this.mantenimientoDAO.findByEmail(email).get();
				mant.setReservasActivas(mant.getReservasActivas()-1);
				this.mantenimientoDAO.save(mant);
			}
			this.vehiculoDAO.save(reserva.getVehiculo());
			this.reservaDAO.save(reserva);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al cancelar la reserva.");
		}
	}

	public void finalizarReserva(String email, String password, String idReserva) {
		Reserva reserva = this.reservaDAO.findById(idReserva).get();
		Parametros parametros = this.parametrosDAO.findById("655b1a3db7bb7908becebbf4").get();
		if (reserva.getEstado().equals("activa") && reserva.getUsuario().getEmail().equals(email)) {
			if (userService.comprobarUsuario(email, password).equals("cliente")) {
				reserva.setEstado("finalizada");
				reserva.getVehiculo().setEstado("disponible");
				reserva.getVehiculo().setBateria(reserva.getVehiculo().getBateria() - parametros.getBateriaViaje());
				if (reserva.getVehiculo().getBateria() < parametros.getMinimoBateria())
					reserva.getVehiculo().setEstado("descargado");
				this.vehiculoDAO.save(reserva.getVehiculo());
				this.reservaDAO.save(reserva);
			}else if (userService.comprobarUsuario(email, password).equals("mantenimiento")) {
				reserva.setEstado("finalizada");
				reserva.getVehiculo().setEstado("disponible");
				reserva.getVehiculo().setBateria(100);
				Mantenimiento mant = this.mantenimientoDAO.findByEmail(email).get();
				mant.setReservasActivas(mant.getReservasActivas()-1);
				this.mantenimientoDAO.save(mant);
				this.vehiculoDAO.save(reserva.getVehiculo());
				this.reservaDAO.save(reserva);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al finalizar la reserva.");
		}
	}

	public void valorarReserva(String email, String password, String idReserva, double valoracion, String comentario) {
		Reserva reserva = this.reservaDAO.findById(idReserva).get();
		if (userService.comprobarUsuario(email, password).equals("cliente") && reserva.getEstado().equals("finalizada")
				&& reserva.getUsuario().getEmail().equals(email)) {
			reserva.setValoracion(valoracion);
			reserva.setComentario(comentario);
			this.reservaDAO.save(reserva);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al valorar la reserva.");
		}
	}

	public List<Reserva> listarReservas(String email, String password) {
		if (userService.comprobarUsuario(email, password).equals("cliente")) {
			List<Reserva> reservas = this.reservaDAO.findByUsuarioEmail(email);
			return reservas;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al listar las reservas.");
		}

	}
}
