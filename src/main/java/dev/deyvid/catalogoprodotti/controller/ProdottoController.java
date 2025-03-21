package dev.deyvid.catalogoprodotti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import dev.deyvid.catalogoprodotti.model.Prodotto;
import dev.deyvid.catalogoprodotti.model.Utente;
import dev.deyvid.catalogoprodotti.service.ProdottiService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes()
public class ProdottoController {
	/*

		--dovrà esporre i metodi che consentano all’utente di navigare verso le 
			pagine creaProdotto.jsp e ricercaProdotti.jsp
		
		--dovrà esporre un metodo per creare un prodotto sul db, sfruttando i Service Spring, 
			e inoltri la response verso la pagina esito.jsp con un opportuno messaggio per differenziare i casi in cui 
			l’inserimento è avvenuto con successo rispetto ai casi in cui si è verificato un errore
			
		--dovrà esporre un metodo per ricercare i prodotti sul db, sfruttando i Service Spring e:
			in caso di errori inoltri la response verso la pagina esito.jsp con un opportuno messaggio
			altrimenti inoltri la response vero la pagina risultatiRicercaProdotti.jsp a cui passerà la List contenente i risultati della ricerca

	 */
	
	@Autowired
	private ProdottiService prodottiService;
	
	@GetMapping("/prodotti")
	public String prodotti(Model model) {
		try {
			return "index";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@GetMapping("/ricercaProdotti")
	public String goToRicercaProdotti() {
		return "ricercaProdotti";
	}
	
	@GetMapping("/creaProdotto")
	public String goToCreaProdotto() {
		return "creaProdotto";
	}
	
	@ModelAttribute("prodotto")
	public Prodotto initEmptyProdotto() {
		return new Prodotto();
	}
	
	
	@PostMapping("/creaProdotto")
	public String creaProdotto(Model model, @ModelAttribute("prodotto") Prodotto parametroProdotto) {
		try {
			String nome = parametroProdotto.getNome();
			Float prezzo = parametroProdotto.getPrezzo();
			Prodotto prodotto = prodottiService.creaProdotto(nome, prezzo);
			String message = "";
			if(prodotto != null) {
				message = "Prodotto creato con successo.";
			}
			else {
				message = "Prodotto già esistente...";
			}
			return "redirect:/esito?message="+message;
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@PostMapping("/ricercaProdotti")
    public String ricercaProdotti(@ModelAttribute("prodotto") Prodotto parametroProdotto, Model model, HttpServletRequest request) {
        try {
            String nome = parametroProdotto.getNome();
            Float prezzo = parametroProdotto.getPrezzo();

            List<Prodotto> prodotti = prodottiService.findByNomeAndPrezzo(nome, prezzo);

            if (prodotti.isEmpty()) {
                model.addAttribute("message", "Nessun prodotto trovato");
                return "esito";
            }
            
            request.setAttribute("prodotti", prodotti);
            return "forward:/risultatiRicercaProdotti";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "ERRORE!!! Ricerca dei prodotti fallita");
            return "esito";
        }
    }

    @PostMapping("/risultatiRicercaProdotti")
    public String risultatiRicercaProdotti(Model model, HttpServletRequest request) {
    	List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
        if (prodotti != null && prodotti.isEmpty()) {
            model.addAttribute("prodotti", prodotti);
        } else {
            model.addAttribute("message", "Nessun prodotto trovato");
        }

        return "risultatiRicercaProdotti";
    }
	
	@GetMapping("/esito")
	public String esito(Model model, HttpServletRequest request) {
		String messaggio = request.getParameter("message");
		if(messaggio != null && !messaggio.isEmpty()) {
			model.addAttribute("message", messaggio);
		}
		return "esito";
	}
	
    @PostMapping("/totalUpdate")
    public String totalUpdate(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        Float prezzo = Float.parseFloat(request.getParameter("prezzo"));

        Prodotto prodotto = prodottiService.getById(id);
        prodotto.setNome(nome);
        prodotto.setPrezzo(prezzo);
        prodottiService.save(prodotto);
        
        return "redirect:/homeInterna";
    }

    @PostMapping("/partialUpdate")
    public String partialUpdate(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String prezzoStr = request.getParameter("prezzo");
        
        Prodotto prodotto = prodottiService.getById(id);
        
        if (nome != null && !nome.isEmpty()) {
            prodotto.setNome(nome);
        }
        
        if (prezzoStr != null && !prezzoStr.isEmpty()) {
            Float prezzo = Float.parseFloat(prezzoStr);
            prodotto.setPrezzo(prezzo);
        }
        
        prodottiService.save(prodotto);
        
        return "redirect:/homeInterna";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        prodottiService.deleteById(id);
        return "redirect:/homeInterna";
    }

}
