package entities;

import org.springframework.data.annotation.Id; 

public class Vehiculo {
	private String tipo;
	private String matricula;
	private String bateria;
	private String modelo;
	private String estado;
	private String direccion;
	
	
	
	public Vehiculo(String tipo, String matricula, String bateria, String modelo, String estado, String direccion) {
		
		this.tipo = tipo;
		this.matricula = matricula;
		this.bateria = bateria;
		this.modelo = modelo;
		this.estado = estado;
		this.direccion = direccion;
	}



	public String getTipo() {
		return tipo;
	}



	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getMatricula() {
		return matricula;
	}



	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}



	public String getBateria() {
		return bateria;
	}



	public void setBateria(String bateria) {
		this.bateria = bateria;
	}



	public String getModelo() {
		return modelo;
	}



	public void setModelo(String modelo) {
		this.modelo = modelo;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String toString() {
		return "Vehiculo [tipo=" + tipo+",matricula="+matricula+",bateria="+bateria+",modelo="+modelo+",estado="+estado+",direccion="+direccion+"]";
	}
	
	
}
