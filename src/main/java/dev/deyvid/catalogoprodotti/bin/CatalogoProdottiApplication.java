package dev.deyvid.catalogoprodotti.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.deyvid.catalogoprodotti.service.UtentiService;
@SpringBootApplication( scanBasePackages = { "dev.deyvid.catalogoprodotti.service", "dev.deyvid.catalogoprodotti.controller", "dev.deyvid.catalogoprodotti.rest"})
@EnableJpaRepositories( basePackages = { "dev.deyvid.catalogoprodotti.repository" })
@EntityScan( basePackages = { "dev.deyvid.catalogoprodotti.model" })
public class CatalogoProdottiApplication {

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext applicationContext = SpringApplication.run(CatalogoProdottiApplication.class, args);
			
			UtentiService utentiService = applicationContext.getBean("utentiService", UtentiService.class);
			utentiService.createRolesIfNotPresent();
			utentiService.createUsersIfNotPresent();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
