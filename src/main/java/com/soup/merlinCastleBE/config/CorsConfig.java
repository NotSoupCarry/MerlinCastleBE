package com.soup.merlinCastleBE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura CORS per le chiamate REST da Angular.
     *
     * In sviluppo Angular gira su localhost:4200.
     * In produzione sostituire con il dominio reale.
     *
     * Nota: il CORS per WebSocket/STOMP è già gestito in WebSocketConfig
     * con setAllowedOrigins(). Questo CorsConfig vale solo per le REST API.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4200"           // Angular dev server
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
