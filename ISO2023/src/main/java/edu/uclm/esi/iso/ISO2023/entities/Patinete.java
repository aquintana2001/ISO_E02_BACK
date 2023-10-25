package edu.uclm.esi.iso.ISO2023.entities;


public class Patinete extends Vehiculo{

	private String color;

	public Patinete(String tipo, String matricula, int bateria, String modelo, String estado, String direccion, String color) {
		super();
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	
}
