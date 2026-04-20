package com.sockets.web.websockertchat.config;

import com.sockets.web.websockertchat.chat.dto.ChatMessage;
import com.sockets.web.websockertchat.chat.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Escucha eventos del ciclo de vida de las sesiones WebSocket.
 *
 * Cuando un usuario cierra la pestaña, pierde la conexión a internet o
 * sale de la aplicación, el WebSocket no recibe un mensaje explícito de
 * "me voy" — simplemente la conexión TCP se cierra. Spring detecta ese
 * cierre y publica internamente un SessionDisconnectEvent.
 *
 * Este listener captura ese evento para notificar al resto de usuarios
 * conectados que alguien abandonó el chat, construyendo y enviando un
 * ChatMessage de tipo LEAVE al canal público.
 *
 * SimpMessageSendingOperations es la interfaz de Spring que permite
 * enviar mensajes al broker desde cualquier parte del código (no solo
 * desde un @MessageMapping), usando convertAndSend().
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSockertEventListener {

    // Permite enviar mensajes al broker (hacia los suscriptores de un topic)
    // desde fuera de un controlador WebSocket.
    private final SimpMessageSendingOperations messageTemplate;

    /**
     * Se dispara automáticamente cuando una sesión WebSocket se desconecta.
     *
     * 1. Extrae los atributos de la sesión STOMP usando StompHeaderAccessor.
     * 2. Recupera el "username" que se guardó cuando el usuario se unió
     *    (ver ChatController.addUser).
     * 3. Si existe el username, construye un mensaje LEAVE y lo publica
     *    en "/topic/public" para que todos los clientes suscritos lo reciban
     *    y muestren "X left!" en el chat.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {
        log.info("WebSocket disconnected");

        // Envuelve el mensaje del evento para poder leer sus cabeceras y atributos de sesión
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // El username fue guardado en la sesión durante el JOIN (ChatController.addUser)
        String userName = (String) headerAccessor.getSessionAttributes().get("username");

        if (userName != null) {
            log.info("User Disconnected : {}", userName);

            // Construye el mensaje de salida usando el builder de Lombok
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(userName)
                    .build();

            // Publica el mensaje en el topic público → todos los suscritos lo recibirán
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}
