package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id;

import edu.uclm.esi.iso.ISO2023.entities.Vehiculo; 

public class Coche extends Vehiculo{
	
	private int nPlaza;

		super(tipo, matricula, bateria, modelo, estado, direccion);
		// TODO Auto-generated constructor stub
	}

	public int getnPlaza() {
		return nPlaza;
	}

	public void setnPlaza(int nPlaza) {
		this.nPlaza = nPlaza;
	}
	
	

}
