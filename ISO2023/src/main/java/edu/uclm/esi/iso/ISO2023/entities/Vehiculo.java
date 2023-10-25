package edu.uclm.esi.iso.ISO2023.entities;

import javax.validation.constraints.NotEmpty;



public class Vehiculo {
	private String matricula;
	private String tipo;
	private String modelo;
	private String estado;
	private String direccion;
	
	
	
		
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



		return bateria;
	}



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
