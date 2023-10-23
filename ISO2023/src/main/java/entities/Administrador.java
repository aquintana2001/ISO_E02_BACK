package entities;

import org.springframework.data.annotation.Id;

public class Administrador extends User{

	public Administrador(String nombre, String apellidos, String email, String password, boolean activo, int intentos) {
		super(nombre, apellidos, email, password, activo, intentos);
		// TODO Auto-generated constructor stub
	}

	public Object getDni() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getTelefono() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCarnet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCiudad() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCiudad(Object ciudad) {
		// TODO Auto-generated method stub
		
	}

	public void setTelefono(Object telefono) {
		// TODO Auto-generated method stub
		
	}

	public void setDni(Object dni) {
		// TODO Auto-generated method stub
		
	}

	public void setCarnet(Object carnet) {
		// TODO Auto-generated method stub
		
	}

	

	


}
