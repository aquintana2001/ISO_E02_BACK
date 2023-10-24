package entities;


public class Cliente extends User{

	private String ciudad;
	private boolean carnet;
	private String telefono;
	private String dni;
	
	public Cliente(String nombre, String apellidos, String email, String password, boolean activo, int intentos, String ciudad,
			boolean carnet, String telefono, String dni) {
		super(nombre, apellidos, email, password, activo, intentos);
		// TODO Auto-generated constructor stub
		
		this.ciudad = "Ciudad Real"; 
		this.carnet = carnet;
		this.telefono=telefono;
		this.dni=dni;
	}

	public String getCiudad() {
		return ciudad;
	}


	public boolean isCarnet() {
		return carnet;
	}


	public void setCarnet(boolean carnet) {
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

	@Override
	public String toString() {
		return "Cliente [dni=" + dni +", ciudad=" + ciudad + ", telefono=" + telefono +"]";
	}

	//Comprobar longitud telefono
	public boolean comprobarNumero(String telefono) {
		if(telefono.length() != 9)
			return false;
		
		//Evitar numeros extranjeros
		//if(telefono.charAt(0) != '6' && telefono.charAt(0) != '7' && telefono.charAt(0)!= '8' && telefono.charAt(0)!='9')
		//	return false;
		
		//Evitar letras en el numero
		for(int i=0;i< telefono.length();i++)
			if(!Character.isDigit(telefono.charAt(i)))
				return false;
		
		return true;
	}
	
	public boolean comprobarDni(String dni) {
		 
		// El DNI debe tener 9 caracteres
		if (dni.length() != 9) {
			return false;
		}

		// Comprobar que los primeros 8 caracteres son dígitos
		for (int i = 0; i < 8; i++) {
			if (!Character.isDigit(dni.charAt(i))) {
				return false;
			}
		}

		// Comprobar que el último carácter es una letra
		char letra = dni.charAt(8);
		if (!Character.isLetter(letra)) {
			return false;
		}

		return true;
	}


		

}
	
	
	
	

