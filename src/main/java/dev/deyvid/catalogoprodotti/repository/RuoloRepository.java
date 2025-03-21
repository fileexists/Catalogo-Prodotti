package dev.deyvid.catalogoprodotti.repository;

import org.springframework.data.repository.CrudRepository;
import dev.deyvid.catalogoprodotti.model.Ruolo;

public interface RuoloRepository extends CrudRepository<Ruolo, Integer>{
	public Ruolo getByNome(String nome);

}
