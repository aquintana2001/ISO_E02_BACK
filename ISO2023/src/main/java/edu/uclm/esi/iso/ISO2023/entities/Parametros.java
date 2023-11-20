package edu.uclm.esi.iso.ISO2023.entities;

public class Parametros {
	private double precioViaje;
	private int minimoBateria;
	private int bateriaViaje;
	private int maxVehiculosMantenimiento;
	
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
