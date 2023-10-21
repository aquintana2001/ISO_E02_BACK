package entities;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tokens")
public class Token {
	@Id @Column(length=36)
	private String id;
	private Long horaCreacion;
	private Long horaConfirmacion;
	@ManyToOne
	private User user;
	
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
