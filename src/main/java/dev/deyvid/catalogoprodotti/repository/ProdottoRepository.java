package dev.deyvid.catalogoprodotti.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import dev.deyvid.catalogoprodotti.model.Prodotto;

public interface ProdottoRepository extends CrudRepository<Prodotto, Integer>{

	public Prodotto getByNome(String nome);
	public List<Prodotto> getByNomeLike(String nome);
    public List<Prodotto> getByPrezzoBetween(Float min, Float max);
    public List<Prodotto> getByNomeContainingAndPrezzoBetween(String nome, Float min, Float max);
    public List<Prodotto> getByPrezzoLessThan(Float prezzo);
    public List<Prodotto> getByNomeContainingAndPrezzoLessThan(String nome, Float prezzo);
    public void deleteByPrezzoBetween(Float min, Float max);
	
}
