package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id; 

public class Coche extends Vehiculo{
	
	private int nPlazas;

	public Coche(String tipo, String matricula, int bateria, String modelo, String estado, String direccion, int nPlazas) {
		super(tipo, matricula, bateria, modelo, estado, direccion);
		this.nPlazas = nPlazas;
	}

	public int getnPlaza() {
		return nPlazas;
	}

	public void setnPlaza(int nPlazas) {
		this.nPlazas = nPlazas;
	}
	
	

}
