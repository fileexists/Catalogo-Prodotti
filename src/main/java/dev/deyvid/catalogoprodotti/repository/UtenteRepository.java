package dev.deyvid.catalogoprodotti.repository;

import org.springframework.data.repository.CrudRepository;
import dev.deyvid.catalogoprodotti.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Integer>{
	
	public Utente getByUsername(String username);
}
