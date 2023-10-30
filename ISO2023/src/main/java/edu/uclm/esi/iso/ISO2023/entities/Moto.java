package edu.uclm.esi.iso.ISO2023.entities;

public class Moto extends Vehiculo {

	private boolean casco;

	public Moto(String tipo, String matricula, int bateria, String modelo, String estado, String direccion,
			boolean casco) {
		super(tipo, matricula, bateria, modelo, estado, direccion);
		this.casco = casco;
	}

	public boolean isCasco() {
		return casco;
	}

	public void setCasco(boolean casco) {
		this.casco = casco;
	}

}
