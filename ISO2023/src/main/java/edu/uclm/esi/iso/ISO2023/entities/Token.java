package edu.uclm.esi.iso.ISO2023.entities;

import java.util.UUID;

public class Token {
	private String id;
	private Long horaCreacion;
	private Long horaConfirmacion;
	private User user;
	private Cliente cliente;
	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Token() {
		this.id=UUID.randomUUID().toString();
		this.horaCreacion=System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getHoraCreacion() {
		return horaCreacion;
	}

	public void setHoraCreacion(Long horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	public Long getHoraConfirmacion() {
		return horaConfirmacion;
	}

	public void setHoraConfirmacion(Long horaConfirmacion) {
		this.horaConfirmacion = horaConfirmacion;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
