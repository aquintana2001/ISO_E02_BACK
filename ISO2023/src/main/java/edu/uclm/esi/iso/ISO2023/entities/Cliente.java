package edu.uclm.esi.iso.ISO2023.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cliente")
public class Cliente extends User {

	private String fechaNacimiento;
	private String carnet;
	private String telefono;
	private String dni;
	private String secretKey;

	public Cliente(String nombre, String apellidos, String email, String password, boolean activo, int intentos,
			String fechaNacimiento, String carnet, String telefono, String dni, String secretKey) {
		super(nombre, apellidos, email, password, activo, intentos);
		this.fechaNacimiento = fechaNacimiento;
		this.carnet = carnet;
		this.telefono = telefono;
		this.dni = dni;
		this.secretKey = secretKey;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;

	}

	public String getCarnet() {
		return carnet;
	}

	public void setCarnet(String carnet) {
		this.carnet = carnet;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getsecretKey() {
		return secretKey;
	}

	public void setsecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	@Override
	public String toString() {
		return "Cliente [dni=" + dni + ", fechs nacimiento=" + fechaNacimiento + ", telefono=" + telefono + ", secretKey=" + secretKey + "]";
	}

	// Comprobar longitud telefono
	public boolean comprobarNumero(String telefono) {
		if (telefono.length() != 9)
			return false;

		// Evitar letras en el numero
		for (int i = 0; i < telefono.length(); i++)
			if (!Character.isDigit(telefono.charAt(i)))
				return false;

		return true;
	}

	public boolean comprobarDni(String dni) {
		boolean valido = true;
		// El DNI debe tener 9 caracteres
		if (dni.length() != 9) {
			valido = false;
		}

		// Comprobar que los primeros 8 caracteres son dígitos
		for (int i = 0; i < 8; i++) {
			if (!Character.isDigit(dni.charAt(i))) {
				valido = false;
			}
		}

		// Comprobar que el último carácter es una letra
		char letra = dni.charAt(8);
		if (!Character.isLetter(letra))
			valido = false;

		return valido;
	}

}
