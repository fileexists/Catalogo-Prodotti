package dev.deyvid.catalogoprodotti.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Utente {
	
	/*
		Deve avere un id numerico (primary-key sul db)
		Deve avere due campi, di tipo stringa, corrispondenti allo username ed alla password 
		ed un campo booleano che servirà ad indicare se l’utente è loggato o meno nella web application
		Deve essere legato da una relazione n-1 con il Ruolo
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	private Boolean loggato;
	
	@ManyToOne
	@JoinColumn( name = "id_ruolo" )
	private Ruolo ruolo;
	
	public Utente() {
		super();
	}
	
	public Utente(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", username=" + username + ", password=" + password + ", loggato=" + loggato + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public Boolean getLoggato() {
		return loggato;
	}

	public void setLoggato(Boolean loggato) {
		this.loggato = loggato;
	}
}
