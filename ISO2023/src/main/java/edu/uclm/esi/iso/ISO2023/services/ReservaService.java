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
import edu.uclm.esi.iso.ISO2023.entities.User;
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
	private MantenimientoDAO mantenimientoDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private ParametrosDAO parametrosDAO;

	private static final String CLIENTE = "cliente";
	private static final String MANTENIMIENTO = "mantenimiento";
	private static final String DISPONIBLE = "disponible";
	private static final String DESCARGADO = "descargado";
	private static final String RESERVADO = "reservado";
	private static final String ACTIVA = "activa";
	private static final String FINALIZADA = "finalizada";
	private static final String ID_BBDD = "655b1a3db7bb7908becebbf4";

	public void realizarReserva(String email, String password, String idVehiculo) {
		String tipoCliente = userService.checkUser(email, password);
		User usuario = userService.findUser(tipoCliente, email);
		Optional<Vehiculo> vehiculo = this.vehiculoDAO.findById(idVehiculo);

		if (vehiculo.isPresent())
			switch (tipoCliente) {
			case CLIENTE:
				reservaCliente(usuario, vehiculo.get());
				break;
			case MANTENIMIENTO:
				reservaMantenimiento(usuario, vehiculo.get());
				break;
			default:
			}
	}

	public void reservaCliente(User usuario, Vehiculo vehiculo) {
		Optional<Cliente> cliente = this.clienteDAO.findByEmail(usuario.getEmail());
		if (vehiculo.getEstado().equals(DISPONIBLE) && cliente.isPresent()) {
			if (this.reservaDAO.findByUsuarioEmailAndVehiculoEstado(usuario.getEmail(), RESERVADO).isEmpty()) {
				comprobarCarnet((Cliente) usuario, vehiculo);
				Reserva reserva = new Reserva(vehiculo, cliente.get(), 0.0);
				vehiculo.setEstado(RESERVADO);
				reserva.setEstado(ACTIVA);
				this.vehiculoDAO.save(vehiculo);
				this.reservaDAO.save(reserva);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ya tienes una reserva activa.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El vehiculo ya está reservado.");
		}
	}

	public void reservaMantenimiento(User usuario, Vehiculo vehiculo) {
		Optional<Mantenimiento> mant = this.mantenimientoDAO.findByEmail(usuario.getEmail());
		Optional<Parametros> par = this.parametrosDAO.findById(ID_BBDD);
		if (vehiculo.getEstado().equals(DESCARGADO) && mant.isPresent() && par.isPresent()) {
			if (mant.get().getReservasActivas() < par.get().getMaxVehiculosMantenimiento()) {
				Reserva reserva = new Reserva(vehiculo, mant.get(), 0.0);
				vehiculo.setEstado("en mantenimiento");
				reserva.setEstado(ACTIVA);
				mant.get().setReservasActivas(mant.get().getReservasActivas() + 1);
				this.vehiculoDAO.save(vehiculo);
				this.reservaDAO.save(reserva);
				this.mantenimientoDAO.save(mant.get());
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No puedes reservar mas vehiculos.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El vehiculo no necesita mantenimiento.");
		}
	}

	public void comprobarCarnet(Cliente cliente, Vehiculo vehiculo) {
		if (vehiculo.getTipo().equals("coche") && !cliente.getCarnet().equals("B")) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Necesitas sacarte el carnet B.");
		} else if (vehiculo.getTipo().equals("moto")
				&& !(cliente.getCarnet().equals("A") || cliente.getCarnet().equals("B"))) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Necesitas sacarte el carnet A o B.");
		}
	}

	public void cancelarReserva(String email, String password, String idReserva) {
		Optional<Reserva> reserva = this.reservaDAO.findById(idReserva);
		Optional<Mantenimiento> mant = this.mantenimientoDAO.findByEmail(email);
		String tipoUser = userService.checkUser(email, password);
		if (reserva.isPresent() && reserva.get().getEstado().equals(ACTIVA)
				&& reserva.get().getUsuario().getEmail().equals(email)) {
			if (tipoUser.equals(CLIENTE)) {
				reserva.get().getVehiculo().setEstado(DISPONIBLE);
			}
			if (tipoUser.equals(MANTENIMIENTO) && mant.isPresent()) {
				mant.get().setReservasActivas(mant.get().getReservasActivas() - 1);
				reserva.get().getVehiculo().setEstado(DESCARGADO);
				this.mantenimientoDAO.save(mant.get());
			}
			reserva.get().setEstado("cancelada");
			this.vehiculoDAO.save(reserva.get().getVehiculo());
			this.reservaDAO.save(reserva.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al cancelar la reserva.");
		}
	}

	

	public void finalizarReserva(String email, String password, String idReserva) {
		Optional<Reserva> reserva = this.reservaDAO.findById(idReserva);
		Optional<Parametros> parametros = this.parametrosDAO.findById(ID_BBDD);
		if (reserva.isPresent() && parametros.isPresent()) {
			if (reserva.get().getEstado().equals(ACTIVA) && reserva.get().getUsuario().getEmail().equals(email)) {
				if (userService.checkUser(email, password).equals(CLIENTE)) {
					finalizarCliente(reserva.get(), parametros.get());
				} else if (userService.checkUser(email, password).equals(MANTENIMIENTO)) {
					finalizarMantenimiento(reserva.get(), email);
				}
				reserva.get().setEstado(FINALIZADA);
				this.vehiculoDAO.save(reserva.get().getVehiculo());
				this.reservaDAO.save(reserva.get());
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al finalizar la reserva.");
			}
		}
	}

	public void finalizarCliente(Reserva reserva, Parametros parametros) {
		reserva.getVehiculo().setEstado(DISPONIBLE);
		reserva.getVehiculo().setBateria(reserva.getVehiculo().getBateria() - parametros.getBateriaViaje());
		if (reserva.getVehiculo().getBateria() < parametros.getMinimoBateria())
			reserva.getVehiculo().setEstado(DESCARGADO);

	}

	public void finalizarMantenimiento(Reserva reserva, String email) {
		Optional<Mantenimiento> mant = this.mantenimientoDAO.findByEmail(email);
		if (mant.isPresent()) {
			reserva.setEstado(FINALIZADA);
			reserva.getVehiculo().setEstado(DISPONIBLE);
			reserva.getVehiculo().setBateria(100);
			mant.get().setReservasActivas(mant.get().getReservasActivas() - 1);
			this.mantenimientoDAO.save(mant.get());
		}
	}

	public void valorarReserva(String email, String password, String idReserva, double valoracion, String comentario) {
		Optional<Reserva> reserva = this.reservaDAO.findById(idReserva);
		if (reserva.isPresent()) {
			if (userService.checkUser(email, password).equals(CLIENTE) && reserva.get().getEstado().equals(FINALIZADA)
					&& reserva.get().getUsuario().getEmail().equals(email)) {
				reserva.get().setValoracion(valoracion);
				reserva.get().setComentario(comentario);
				this.reservaDAO.save(reserva.get());
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al valorar la reserva.");
			}
		}
	}

	public List<Reserva> listarReservas(String email, String password) {
		List<Reserva> reservas = this.reservaDAO.findByUsuarioEmail(email);
		if (userService.checkUser(email, password).equals(CLIENTE)) {
			return reservas;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al listar las reservas.");
		}

	}
}
