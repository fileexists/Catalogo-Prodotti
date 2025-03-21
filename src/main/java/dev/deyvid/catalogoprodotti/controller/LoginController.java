package dev.deyvid.catalogoprodotti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import dev.deyvid.catalogoprodotti.model.Utente;
import dev.deyvid.catalogoprodotti.service.UtentiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes({"utenteSessione"})
public class LoginController {
	
	/*

	--dovrà gestire l’informazione dell’utente (loggato o non ancora loggato) nella sessione web
	
	--dovrà esporre i metodi che consentano all’utente di navigare verso le pagine paginaLogin.jsp 
		e paginaRegistrazione.jsp
		
	--deve esporre un metodo login che controlli sul database, servendosi dei Service Spring, 
		la presenza dell’utente con username e password provenienti dalla paginaLogin.jsp e:
			nel caso sia presente, deve mettere l’oggetto Utente con attributo «logged=true» 
			nella sessione web e inoltrare la response sulla pagina homeInterna.jsp
			altrimenti deve presentare un messaggio all’utente per indicare che 
			il login è fallito e re-inoltrare la response su paginaLogin.jsp

	--dovrà esporre un metodo logout che imposti l’attributo logged a false per 
		l’Utente in sessione e inoltri la response su paginaLogin.jsp
		
	--dovrà esporre un metodo registra che salvi su db, tramite i Service Spring, 
		l’utente associato ai campi provenienti dalla paginaRegistrazione.jsp e:
			controlli se l’utente con lo username proveniente dalla jsp è già presente su db ed in tal caso inoltri 
			la response a paginaRegistrazione.jsp con un messaggio opportuno
				in caso di inserimento avvenuto successo: inoltri la response verso paginaLogin.jsp con un opportuno messaggio di avviso
				in caso di altri errori durante l’inserimento inoltri la response verso paginaRegistrazione.jsp  con un opportuno messaggio di avviso


	 */
	
	@Autowired
	private UtentiService utentiService;
	
	@GetMapping("/")
	public String getHomePage(Model model) {
		try {
			return "index";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@ModelAttribute("utenteSessione")
	public Utente initForm() {
		return new Utente();
	}
	
	@ModelAttribute("utente")
	public Utente initEmptyUtente() {
		return new Utente();
	}
		
	@GetMapping("/paginaRegistrazione")
	public String goToFormRegister() {
		return "paginaRegistrazione";
	}
	
	@GetMapping("/paginaLogin")
	public String goToFormLogin(Model model, HttpServletRequest request) {
		//String messaggio = (String) request.getAttribute("messaggio");
		String messaggio = request.getParameter("message");
		if(messaggio != null && !messaggio.isEmpty()) {
			model.addAttribute("message", messaggio);
		}
		return "paginaLogin";
	}
	
	
	@PostMapping("/login")
	public String login(
		Model model, 
		@ModelAttribute("utenteSessione") Utente parametriUtente
	) {
		try {
			String username = parametriUtente.getUsername();
			String password = parametriUtente.getPassword();
			Utente utente = utentiService.login(username, password);
			if(utente == null) {
				model.addAttribute("loginError", "Username o Password errati");
				return "paginaLogin";
			}
			utente.setLoggato(true);
			model.addAttribute("utenteSessione", utente);
			return "redirect:/homeInterna";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@PostMapping("/register")
	public String register(Model model, @ModelAttribute("utenteSessione") Utente parametriUtente) {
		try {
			String username = parametriUtente.getUsername();
			String password = parametriUtente.getPassword();
			Utente utente = utentiService.register(username, password);
			if(utente == null) {
				model.addAttribute("registrazioneError", "Username già esistente");
				return "paginaRegistrazione";
			}
			String message = "Registrazione avvenuta con successo, ora puoi loggarti";
			return "redirect:/paginaLogin?message="+message;
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@GetMapping("/homeInterna")
	public String homeInterna(@ModelAttribute("utenteSessione") Utente utente, Model model) {
		try {
			if (utente.getLoggato() == null || !utente.getLoggato()) {
		        return "redirect:/paginaLogin";
		    }
		    model.addAttribute("utente", utente);
		    return "homeInterna";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@GetMapping("/logout")
	public String logout(@ModelAttribute("utenteSessione") Utente utente, Model model) {
		try {
			if (utente.getLoggato() == null || !utente.getLoggato()) {
		        return "redirect:/paginaLogin";
		    }
			utente.setLoggato(false);
		    return "paginaLogin";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}

}
