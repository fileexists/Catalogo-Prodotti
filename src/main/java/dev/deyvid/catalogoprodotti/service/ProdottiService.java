package dev.deyvid.catalogoprodotti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.deyvid.catalogoprodotti.model.Prodotto;
import dev.deyvid.catalogoprodotti.repository.ProdottoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProdottiService {

	@Autowired
	private ProdottoRepository prodottoRepository;

	public Prodotto getByNome(String nome) {
		return prodottoRepository.getByNome(nome);
	}
	
	public void deleteById(Integer id) {
		prodottoRepository.deleteById(id);
	}
	
	public Prodotto getById(Integer id) {
		return prodottoRepository.findById(id).orElse(null);
	}
	
    public Prodotto save(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }
	
	public Prodotto creaProdotto(String nome, Float prezzo) {
		Prodotto prodotto = getByNome(nome);
		if (prodotto != null || prezzo < 0) {
			return null;
		}
		prodotto = new Prodotto(nome, prezzo);
		prodottoRepository.save(prodotto);
		return prodotto;
	}

	public List<Prodotto> getByNomeLike(String nome) {
		return prodottoRepository.getByNomeLike("%" + nome + "%");
	}
	
	public List<Prodotto> getAll(){
		return (List<Prodotto>) prodottoRepository.findAll();
	}

	public List<Prodotto> getByPrezzo(Float prezzo) {
		return prodottoRepository.getByPrezzoBetween(prezzo - 0.01f, prezzo + 0.01f);
	}
	
	public List<Prodotto> getByPrezzoLessThan(Float prezzo) {
		return prodottoRepository.getByPrezzoLessThan(prezzo + 0.1f);
	}
	
	public List<Prodotto> getByNomeContainingAndPrezzo(String nome, Float prezzo) {
		return prodottoRepository.getByNomeContainingAndPrezzoBetween(nome, prezzo - 0.01f, prezzo + 0.01f);
	}
	
	public void deleteByPriceBetween(Float min, Float max) {
		prodottoRepository.deleteByPrezzoBetween(min -0.1f, max + 0.01f);
	}

	public List<Prodotto> findByNomeAndPrezzo(String nome, Float prezzo) {
		if (nome != null && !nome.isEmpty() && prezzo != null) {
			return getByNomeContainingAndPrezzo(nome, prezzo);
		} else if (nome != null && !nome.isEmpty()) {
			return getByNomeLike(nome);
		} else if (prezzo != null) {
			return getByPrezzo(prezzo);
		} else {
			return (List<Prodotto>) prodottoRepository.findAll();
		}
	}
	
    public List<Prodotto> findByNomeAndPrezzoLessThan(String nome, Float prezzo) {
        if (nome != null && !nome.isEmpty() && prezzo != null) {
            return prodottoRepository.getByNomeContainingAndPrezzoLessThan(nome, prezzo);
        }
		return null;
    }
    
	public Prodotto aggiornamentoCompleto(Prodotto prodotto) {
		Prodotto prodottoDatabase = getById(prodotto.getId());
		if(prodottoDatabase != null) {
			prodottoDatabase.setNome(prodotto.getNome());
			if(prodotto.getPrezzo() == null || prodotto.getPrezzo() > 0) {
				prodottoDatabase.setPrezzo(prodotto.getPrezzo());
			}
		}
		return prodottoDatabase;
	}
	
	public Prodotto aggiornamentoParziale(Prodotto prodotto) {
		Prodotto prodottoDatabase = getById(prodotto.getId());
		if(prodottoDatabase != null) {
			if(prodotto.getNome() != null && !prodotto.getNome().isEmpty()) {
				prodottoDatabase.setNome(prodotto.getNome());
			}
			if(prodotto.getPrezzo() != null && prodotto.getPrezzo() > 0) {
				prodottoDatabase.setPrezzo(prodotto.getPrezzo());
			}
		}
		return prodottoDatabase;
	}

}
