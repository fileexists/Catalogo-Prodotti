package dev.deyvid.catalogoprodotti.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity
public class Ruolo {

	/*
		Deve avere un id numerico (primary-key sul db)
		Deve avere un nome, come stringa
	 */
	@Id
	@GeneratedValue ( strategy=GenerationType.IDENTITY )
	private Integer id;
	
	@Column( unique = true)
	private String nome;
	
	@OneToMany ( mappedBy = "ruolo" )
	private List<Utente> utenti = new ArrayList<Utente>();
	
	public Ruolo() {
		super();
	}
	
	public Ruolo(String nome) {
		super();
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return "Ruolo [id=" + id + ", nome=" + nome + "]";
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
