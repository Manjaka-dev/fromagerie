package itu.fromagerie.fromagerie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Fromagerie - Gestion des Livraisons")
                        .description("API REST pour la gestion complète des livraisons dans une fromagerie. " +
                                   "Cette API permet de gérer les livraisons, les livreurs, les commandes et " +
                                   "le suivi des statuts de livraison.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe Fromagerie")
                                .email("contact@fromagerie.com")
                                .url("https://fromagerie.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement"),
                        new Server()
                                .url("https://api.fromagerie.com")
                                .description("Serveur de production")
    }
}
