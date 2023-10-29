package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
	@Id
	private String email;
	private String nombre;
	private String apellidos;
	private String password;
	private boolean activo;
	private int intentos;

	public User(String nombre, String apellidos, String email, String password, boolean activo, int intentos) {

		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.activo = activo;
		this.intentos = intentos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public String toString() {
		return "User [nombre=" + nombre + ",apellidos=" + apellidos + ",email=" + email + ",password=" + password
				+ ",activo=" + activo + ",intentos=" + intentos + "]";
	}

}
