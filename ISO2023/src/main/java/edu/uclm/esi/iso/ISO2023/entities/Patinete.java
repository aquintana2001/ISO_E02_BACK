package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id; 

public class Patinete extends Vehiculo{

	private String color;

	public Patinete(String tipo, String matricula, int bateria, String modelo, String estado, String direccion, String color) {
		super(tipo, matricula, bateria, modelo, estado, direccion);
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	
}
