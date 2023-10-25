package edu.uclm.esi.iso.ISO2023.entities;


import org.springframework.data.annotation.Id;

import edu.uclm.esi.iso.ISO2023.entities.Vehiculo; 


public class Moto extends Vehiculo{

	private boolean casco;

	public Moto(String tipo, String matricula, int bateria, String modelo, String estado, String direccion) {
		super(tipo, matricula, bateria, modelo, estado, direccion);
		// TODO Auto-generated constructor stub
	}

	public boolean isCasco() {
		return casco;
	}

	public void setCasco(boolean casco) {
		this.casco = casco;
	}
	
	
	
}
