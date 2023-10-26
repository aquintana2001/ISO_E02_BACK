package edu.uclm.esi.iso.ISO2023.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Vehiculo {
	@Id
	private String id;
	private String matricula;
	private String tipo;
	private int bateria;
	private String modelo;
	private String estado;
	private String direccion;
	
	
	
	public Vehiculo(String tipo, String matricula, int bateria, String modelo, String estado, String direccion) {
		this.id = UUID.randomUUID().toString();
		this.tipo = tipo;
		this.matricula = matricula;
		this.bateria = bateria;
		this.modelo = modelo;
		this.estado = estado;
		this.direccion = direccion;
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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



	public int getBateria() {
		return bateria;
	}



	public void setBateria(int bateria) {
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
