package edu.uclm.esi.iso.ISO2023.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;

public class Parametros {
	@Id
	private String id;
	private double precioViaje;
	private int minimoBateria;
	private int bateriaViaje;
	private int maxVehiculosMantenimiento;
	
	public Parametros(double precioViaje, int minimoBateria, int bateriaViaje, int maxVehiculosMantenimiento) {
		this.id = UUID.randomUUID().toString();
		this.precioViaje = precioViaje;
		this.minimoBateria = minimoBateria;
		this.bateriaViaje = bateriaViaje;
		this.maxVehiculosMantenimiento = maxVehiculosMantenimiento;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getPrecioViaje() {
		return precioViaje;
	}
	public void setPrecioViaje(double precioViaje) {
		this.precioViaje = precioViaje;
	}
	public int getMinimoBateria() {
		return minimoBateria;
	}
	public void setMinimoBateria(int minimoBateria) {
		this.minimoBateria = minimoBateria;
	}
	public int getBateriaViaje() {
		return bateriaViaje;
	}
	public void setBateriaViaje(int bateriaViaje) {
		this.bateriaViaje = bateriaViaje;
	}
	public int getMaxVehiculosMantenimiento() {
		return maxVehiculosMantenimiento;
	}
	public void setMaxVehiculosMantenimiento(int maxVehiculosMantenimiento) {
		this.maxVehiculosMantenimiento = maxVehiculosMantenimiento;
	}
}
