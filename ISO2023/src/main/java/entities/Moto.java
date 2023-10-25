package entities;


import org.springframework.data.annotation.Id; 


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
