package entities;

import org.springframework.data.annotation.Id; 

public class Coche extends Vehiculo{
	
	private int nPlaza;

	public Coche(String tipo, String matricula, int bateria, String modelo, String estado, String direccion) {
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
