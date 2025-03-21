package dev.deyvid.catalogoprodotti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.deyvid.catalogoprodotti.model.Ruolo;
import dev.deyvid.catalogoprodotti.model.Utente;
import dev.deyvid.catalogoprodotti.repository.RuoloRepository;
import dev.deyvid.catalogoprodotti.repository.UtenteRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UtentiService {
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private RuoloRepository ruoloRepository;
	
	public Utente getByUsername(String username) {
		return utenteRepository.getByUsername(username);
	}
	
	public Utente login(String username, String password) {
		Utente u = getByUsername(username);
		if (u == null || !u.getPassword().equals(password)) {
			return null;
		}
		
		return u;
	}
	
	public Utente register(String username, String password) {
		Utente u = getByUsername(username);
		if (u != null) {
			return null;
		}
		Utente nuovoUtente = new Utente(username, password);
		Ruolo utenteRuolo = ruoloRepository.getByNome("utente");
		if(utenteRuolo != null) {
			nuovoUtente.setRuolo(utenteRuolo);
		}
		utenteRepository.save(nuovoUtente);
		return nuovoUtente;
	}
	
	public void createRolesIfNotPresent() {
		Ruolo admin = ruoloRepository.getByNome("admin");
		Ruolo utente = ruoloRepository.getByNome("utente");
		if(admin == null) {
			admin = new Ruolo("admin");
			ruoloRepository.save(admin);
		}
		if (utente == null) {
			utente = new Ruolo("utente");
			ruoloRepository.save(utente);
		}
	}
	
	public void createUsersIfNotPresent() {
		Utente utenteAdmin = getByUsername("admin");
		Ruolo ruoloAdmin = ruoloRepository.getByNome("admin");		
		if(utenteAdmin == null) {
			utenteAdmin = new Utente("admin", "admin_pass");
			utenteAdmin.setRuolo(ruoloAdmin);
			utenteRepository.save(utenteAdmin);
		}
	}
}
