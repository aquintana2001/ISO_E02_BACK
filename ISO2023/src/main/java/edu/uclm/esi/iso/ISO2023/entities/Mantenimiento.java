package edu.uclm.esi.iso.ISO2023.entities;

public class Mantenimiento extends User{
	int reservasActivas;
	
	public Mantenimiento(String nombre, String apellidos, String email, String password, boolean activo, int intentos) {
		super(nombre, apellidos, email, password, activo, intentos);
		reservasActivas=0;
	}
	
	public int getReservasActivas() {
		return reservasActivas;
	}
	public void setReservasActivas(int reservasActivas) {
		this.reservasActivas = reservasActivas;
	}
}
