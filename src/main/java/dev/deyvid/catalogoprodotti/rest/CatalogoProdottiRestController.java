package dev.deyvid.catalogoprodotti.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.deyvid.catalogoprodotti.model.Prodotto;
import dev.deyvid.catalogoprodotti.model.Utente;
import dev.deyvid.catalogoprodotti.service.ProdottiService;
import dev.deyvid.catalogoprodotti.service.UtentiService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/v1/prodotti", produces = "application/json")
@CrossOrigin(origins = "*")
@Validated
public class CatalogoProdottiRestController {

	/*
		Le funzionalità rest dovranno controllare negli header la presenza delle credenziali 
		(username/password) e tutte le operazioni di lettura dovranno essere abilitate a qualsiasi 
		utente presente nel db, mentre quelle di scrittura solo agli utenti 
		presenti nel db con ruolo admin


		Le funzionalità Rest dovranno restituire i codici:
			400/BadRequest: in caso di credenziali mancanti
			401/Unauthorize: in caso di utente non presente nel db
			403/Forbidden: per le operazioni di scrittura, in caso di utente 
			presente sul db ma non con ruolo admin

		visualizzazione di tutti i prodotti presenti nel db
		visualizzazione di un prodotto in base all’id fornito come parametro dell’url
		visualizzazione di tutti i prodotti che contengano nel nome una stringa fornita come parametro dell’url
		visualizzazione di tutti i prodotti il cui prezzo sia minore ad un parametro fornito nell’url
		visualizzazione di tutti i prodotti il cui nome contenga un parametro dell’url ed il cui prezzo sia minore di un altro parametro dell’url
		salvataggio di un nuovo prodotto nel db
		aggiornamento completo di un prodotto
		aggiornamento parziale di un prodotto
		cancellazione di un prodotto in base all’id fornito come parametro dell’url
		cancellazione di tutti i prodotti con prezzo compreso tra due parametri forniti nell’url

	 */
	
	@Autowired
	private UtentiService utentiService;
	
	@Autowired
	private ProdottiService prodottiService;
	
	
    public ResponseEntity<Void> checkLoginAndRole(String username, String password, boolean lettura) {
    	try {
	        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
	
	        Utente utente = utentiService.getByUsername(username);
	        
	        if (utente == null || !utente.getPassword().equals(password)) {
	            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        }
	        
	        if (!lettura && (utente.getRuolo() == null || !utente.getRuolo().getNome().equalsIgnoreCase("admin"))) {
	            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	        }
	        
	        return new ResponseEntity<>(HttpStatus.OK);

    	} catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
	@GetMapping("/getAll")
	public ResponseEntity<List<Prodotto>> getAll(@RequestHeader HttpHeaders httpHeaders){
		try	{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, true);

			if(loginResponse.getStatusCode() == HttpStatus.OK) {
				List<Prodotto> prodotti = prodottiService.getAll();
				HttpStatus httpStatus = prodotti != null && prodotti.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				return new ResponseEntity<List<Prodotto>>(prodotti,httpStatus);
			}
			return new ResponseEntity<List<Prodotto>>(loginResponse.getStatusCode());

		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Prodotto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<Prodotto> getById(
		@NotNull(message = "Campo id obbligatorio") 
		@Min(value = 1, message = "L'id deve essere maggiore di zero") 
		@PathVariable("id") Integer id,
		@RequestHeader HttpHeaders httpHeaders
	){
		try	{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, true);
			if(loginResponse.getStatusCode() == HttpStatus.OK) {
				Prodotto prodotto = prodottiService.getById(id);
				HttpStatus httpStatus = prodotto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				return new ResponseEntity<Prodotto>(prodotto,httpStatus);
			}
			return new ResponseEntity<Prodotto>(loginResponse.getStatusCode());

		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Prodotto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getByNome/{nome}")
	public ResponseEntity<List<Prodotto>> getByNome(
		@NotEmpty(message = "Campo nome obbligatorio") 
		@PathVariable("nome") String nome,
		@RequestHeader HttpHeaders httpHeaders
	) {
		try	{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, true);
			if(loginResponse.getStatusCode() == HttpStatus.OK) {
				List<Prodotto> prodotti = prodottiService.getByNomeLike(nome);
				HttpStatus httpStatus = prodotti != null && prodotti.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				return new ResponseEntity<List<Prodotto>>(prodotti,httpStatus);
			}
			return new ResponseEntity<List<Prodotto>>(loginResponse.getStatusCode());
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Prodotto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getByPrezzoMinoreDi/{prezzo}")
	public ResponseEntity<List<Prodotto>> getByPrezzoMinoreDi(
		@NotNull(message = "Campo prezzo obbligatorio") 
		@PathVariable("prezzo") Float prezzo,
		@RequestHeader HttpHeaders httpHeaders
	) {
		try	{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, true);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
	        	List<Prodotto> prodotti = prodottiService.getByPrezzoLessThan(prezzo);
				HttpStatus httpStatus = prodotti != null && prodotti.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
				return new ResponseEntity<List<Prodotto>>(prodotti,httpStatus);
			}
			return new ResponseEntity<List<Prodotto>>(loginResponse.getStatusCode());	        
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Prodotto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getByNomeAndPrezzo/{nome}/{prezzo}")
	public ResponseEntity<List<Prodotto>> getByNomeAndPrezzo(
	    @NotEmpty(message = "Campo nome obbligatorio") 
	    @PathVariable("nome") String nome,
	    @NotNull(message = "Campo prezzo obbligatorio") 
	    @PathVariable("prezzo") Float prezzo,
	    @RequestHeader HttpHeaders httpHeaders
	) {
	    try {
	        String username = httpHeaders.getFirst("username");
	        String password = httpHeaders.getFirst("password");

	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, true);
	        if (loginResponse.getStatusCode() == HttpStatus.OK) {
	            List<Prodotto> prodotti = prodottiService.findByNomeAndPrezzoLessThan(nome, prezzo);
	            HttpStatus httpStatus = (prodotti != null && !prodotti.isEmpty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
	            return new ResponseEntity<>(prodotti, httpStatus);
	        }

	        return new ResponseEntity<>(loginResponse.getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PostMapping(path = "/insert", consumes = "application/json")
	public ResponseEntity<Prodotto> insertLocalita(
		@Valid
		@RequestBody Prodotto prodotto, 
		@RequestHeader HttpHeaders httpHeaders
	) {
	    try {
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, false);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
	        	Prodotto newProdotto = prodottiService.save(prodotto);
    	        return new ResponseEntity<>(newProdotto, HttpStatus.CREATED);
	        }
	        return new ResponseEntity<>(loginResponse.getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	@PutMapping(path = "/totalUpdate", consumes = "application/json")
	public ResponseEntity<Prodotto> totalUpdate(@RequestBody Prodotto prodotto, @RequestHeader HttpHeaders httpHeaders) {
	    try {
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, false);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
	        	Prodotto prodottodb = prodottiService.aggiornamentoCompleto(prodotto);
		    	if(prodottodb == null) {
	    	        return new ResponseEntity<Prodotto>(HttpStatus.NOT_FOUND);
		    	}
    	        return new ResponseEntity<Prodotto>(prodottodb, HttpStatus.CREATED);
	        }
	        return new ResponseEntity<Prodotto>(loginResponse.getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PatchMapping(path = "/partialUpdate", consumes = "application/json")
	public ResponseEntity<Prodotto> partialUpdate(@RequestBody Prodotto prodotto, @RequestHeader HttpHeaders httpHeaders) {
	    try {
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, false);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
		    	Prodotto prodottodb = prodottiService.aggiornamentoParziale(prodotto);
		    	if(prodottodb == null) {
	    	        return new ResponseEntity<Prodotto>(HttpStatus.NOT_FOUND);
		    	}
    	        return new ResponseEntity<Prodotto>(prodottodb, HttpStatus.CREATED);
	        }
	        return new ResponseEntity<Prodotto>(loginResponse.getStatusCode());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping(path="/deleteById/{id}")
	public ResponseEntity<Void> cancella(
		@NotNull(message =  "Il campo id è obbligatorio")
		@Min(value = 1, message = "Ammessi solo id positivi") 
		@PathVariable("id") Integer id,
		@RequestHeader HttpHeaders httpHeaders
	) {
		try{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, false);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
		    	prodottiService.deleteById(id);
    	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(loginResponse.getStatusCode());
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@DeleteMapping(path="/deleteByPrezzoBetween/{min}/{max}")
	public ResponseEntity<Void> deleteByPrezzoBetween(
		@NotNull(message =  "Il campo min è obbligatorio")
		@Min(value = 0, message = "Ammessi solo prezzi positivi") 
		@PathVariable("min") Float min,
		@NotNull(message =  "Il campo id è obbligatorio")
		@PathVariable("max") Float max,		
		@RequestHeader HttpHeaders httpHeaders
	) {
		try{
			String username = httpHeaders.getFirst("username");
			String password = httpHeaders.getFirst("password");
			
	        ResponseEntity<Void> loginResponse = checkLoginAndRole(username, password, false);
	        if(loginResponse.getStatusCode() == HttpStatus.OK) {
		    	prodottiService.deleteByPriceBetween(min, max);
    	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(loginResponse.getStatusCode());
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,String> handleMethodArgumentException(MethodArgumentNotValidException e){
		Map<String,String> result = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach( oe -> {
			result.put(oe.getField(),oe.getDefaultMessage());
		});
		return result;
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,String> handleConstraintViolationException(ConstraintViolationException e){
		Map<String,String> result = new HashMap<>();
		e.getConstraintViolations().forEach( ce -> {
			result.put(ce.getPropertyPath().toString(),ce.getMessage());
		});
		return result;
	}	
}
