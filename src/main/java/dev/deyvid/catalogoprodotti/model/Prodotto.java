package dev.deyvid.catalogoprodotti.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Prodotto {
	/*
		Deve avere un id numerico (primary-key sul db)
		Deve avere un campo, di tipo stringa, corrispondente al nome
		Deve avere un campo, di tipo Float, corrispondente al prezzo
	 */

	@Id
	@GeneratedValue ( strategy=GenerationType.IDENTITY )
	private Integer id;
	
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private String nome;
	
	@JsonProperty(access = JsonProperty.Access.AUTO)
	private Float prezzo;
	
	public Prodotto() {
		super();
	}
	
	public Prodotto(String nome, Float prezzo) {
		super();
		this.nome = nome;
		this.prezzo = prezzo;
	}
	
	@Override
	public String toString() {
		return "Prodotto [id=" + id + ", nome=" + nome + ", prezzo=" + prezzo + "]";
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
	
	public Float getPrezzo() {
		return prezzo;
	}
	
	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}
	
	
	
}
