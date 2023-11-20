package edu.uclm.esi.iso.ISO2023.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Reserva {
	@Id
	private String id;
	private Vehiculo vehiculo;
	private Cliente cliente;
	private double valoracion;
	private String estado;
	
	public Reserva(Vehiculo vehiculo, Cliente cliente, double valoracion) {
		this.id = UUID.randomUUID().toString();
		this.vehiculo = vehiculo;
		this.cliente=cliente;
		this.valoracion=valoracion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getValoracion() {
		return valoracion;
	}

	public void setValoracion(double valoracion) {
		this.valoracion = valoracion;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
