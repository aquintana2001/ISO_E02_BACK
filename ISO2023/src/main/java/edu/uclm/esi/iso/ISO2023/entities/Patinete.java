package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id; 

public class Patinete extends Vehiculo{

	private String color;

	public Patinete(String tipo, String matricula, int bateria, String modelo, String estado, String direccion) {
		super(tipo, matricula, bateria, modelo, estado, direccion);
		// TODO Auto-generated constructor stub
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	
}
