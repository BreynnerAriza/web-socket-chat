package com.sockets.web.websockertchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración central del WebSocket con STOMP.
 *
 * Un WebSocket es un protocolo de comunicación bidireccional y persistente entre
 * el cliente (navegador) y el servidor. A diferencia de HTTP, donde el cliente
 * siempre inicia la petición, con WebSocket ambos lados pueden enviarse mensajes
 * en cualquier momento sin necesidad de hacer una nueva solicitud.
 *
 * STOMP (Simple Text Oriented Messaging Protocol) es un protocolo de mensajería
 * que se monta encima de WebSocket. Define el formato de los mensajes (destinos,
 * cabeceras, cuerpo) y facilita el sistema de publicación/suscripción (pub/sub).
 *
 * SockJS es una librería de fallback: si el navegador del cliente no soporta
 * WebSocket nativo, SockJS emula la conexión usando otras técnicas (long-polling,
 * etc.) de forma transparente.
 *
 * @EnableWebSocketMessageBroker activa el sistema de mensajería con broker
 * sobre WebSocket en Spring.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registra el endpoint al que el cliente se conecta para iniciar
     * la sesión WebSocket.
     *
     * El frontend se conecta a: new SockJS('/ws')
     * Esa URL '/ws' es exactamente el endpoint que se registra aquí.
     * .withSockJS() habilita el fallback automático para navegadores
     * que no soporten WebSocket nativo.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Configura el broker de mensajes (enrutador interno de mensajes).
     *
     * setApplicationDestinationPrefixes("/app"):
     *   Todo mensaje enviado desde el cliente con destino que empiece por "/app"
     *   será enrutado a un método @MessageMapping del controlador.
     *   Ejemplo: el cliente envía a "/app/chat.sendMessage" → llega a ChatController.
     *
     * enableSimpleBroker("/topic"):
     *   Activa el broker simple en memoria de Spring. Los clientes pueden
     *   suscribirse a destinos que empiecen por "/topic" para recibir mensajes.
     *   Ejemplo: el cliente se suscribe a "/topic/public" → recibe todos los
     *   mensajes que el servidor envíe a ese destino.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.setApplicationDestinationPrefixes("/app");
       registry.enableSimpleBroker("/topic");
    }

}
