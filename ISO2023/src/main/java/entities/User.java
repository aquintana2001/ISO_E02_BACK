package entities;


import java.util.UUID; 

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;


@Entity 
@Table(
		name = "users", 
		indexes = {
				@Index(columnList = "email", unique=true)
		})


public class User {
	@Id @Column(length=50) @NotEmpty
	private String email;
	@Column(length = 50) @NotEmpty
	private String nombre;
	@Column(length=50) @NotEmpty
	private String apellidos;
	@Column(length=32) @NotEmpty
	private String password;
	@Column(length=1) 
	private boolean activo;
	@Column(length=1) 
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
		return nombre;
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
}
