package com.sockets.web.websockertchat.chat.controller;

import com.sockets.web.websockertchat.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * Controlador de mensajes WebSocket/STOMP del chat.
 *
 * A diferencia de un @RestController que atiende peticiones HTTP,
 * este controlador atiende mensajes STOMP enviados por los clientes
 * a través de la conexión WebSocket ya establecida.
 *
 * Flujo general:
 *   Cliente → envía mensaje STOMP a "/app/chat.X"
 *   Spring → quita el prefijo "/app" y busca un @MessageMapping("/chat.X")
 *   Método → procesa el mensaje y retorna la respuesta
 *   @SendTo → Spring reenvía la respuesta a todos los suscritos al topic indicado
 */
@Controller
public class ChatController {

    /**
     * Recibe un mensaje de chat y lo difunde a todos los usuarios conectados.
     *
     * El cliente envía a: "/app/chat.sendMessage"  (con el prefijo /app configurado en WebSocketConfig)
     * Spring enruta a este método quitando el prefijo, buscando "/chat.sendMessage".
     * @SendTo("/topic/public") hace que el valor retornado se publique en ese topic,
     * y todos los clientes suscritos a "/topic/public" lo recibirán en tiempo real.
     *
     * @Payload deserializa automáticamente el JSON del cuerpo del mensaje a ChatMessage.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    /**
     * Registra a un nuevo usuario en el chat y notifica a todos los demás.
     *
     * El cliente envía a: "/app/chat.addUser" con un payload tipo:
     *   { "sender": "nombreUsuario", "type": "JOIN" }
     *
     * Además de difundir el mensaje JOIN al topic público, este método
     * guarda el username en los atributos de la sesión STOMP. Esto es
     * clave para poder identificar al usuario cuando se desconecte, ya
     * que la desconexión no lleva payload — solo se tiene la sesión.
     * El WebSockertEventListener recuperará ese username al detectar la desconexión.
     *
     * @param headerAccessor permite acceder y modificar los atributos de la sesión STOMP activa.
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Guarda el username en la sesión para poder recuperarlo al desconectarse
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        return chatMessage;
    }

}
