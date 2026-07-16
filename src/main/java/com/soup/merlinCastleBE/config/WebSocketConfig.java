package com.soup.merlinCastleBE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura il message broker STOMP.
     *
     * /topic  → broadcast (server → tutti i client della stanza)
     * /queue  → messaggi privati (server → singolo client, es. rivelazione ruolo)
     * /app    → prefisso per i messaggi in ingresso dai client verso i @MessageMapping
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Broker in-memory: gestisce /topic e /queue
        registry.enableSimpleBroker("/topic", "/queue");

        // Prefisso per i messaggi che arrivano dai client e vengono instradati
        // verso i metodi @MessageMapping nei controller
        registry.setApplicationDestinationPrefixes("/app");

        // Prefisso per i messaggi privati (user-specific)
        // Usato da SimpMessagingTemplate.convertAndSendToUser(...)
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Registra l'endpoint WebSocket al quale i client si connettono.
     *
     * Il client Angular usa:
     *   new SockJS('http://localhost:8080/ws')
     *
     * SockJS è un fallback per browser che non supportano WS nativo,
     * ma tutti i browser moderni useranno WebSocket direttamente.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // Origini permesse in sviluppo (Angular dev server gira su 4200)
                // In produzione sostituire con il dominio reale es. "https://avalon.tuffgramma.com"
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS(); // abilita SockJS come fallback
    }
}
