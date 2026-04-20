package com.sockets.web.websockertchat.chat.dto;

import com.sockets.web.websockertchat.chat.enums.MessageType;
import lombok.*;

/**
 * Objeto de transferencia de datos (DTO) que representa un mensaje del chat.
 *
 * Esta clase es el "contrato" entre el frontend y el backend: el cliente
 * envía y recibe JSON con exactamente estos campos. Spring los serializa/
 * deserializa automáticamente usando Jackson.
 *
 * Ejemplo de JSON que viaja por el WebSocket:
 *   { "sender": "Juan", "content": "Hola a todos!", "type": "CHAT" }
 *   { "sender": "Juan", "type": "JOIN" }
 *   { "sender": "Juan", "type": "LEAVE" }
 *
 * Las anotaciones de Lombok evitan escribir constructores, getters y setters
 * manualmente. @Builder permite construir el objeto con el patrón builder:
 *   ChatMessage.builder().sender("Juan").type(MessageType.LEAVE).build()
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;    // Texto del mensaje (solo presente en mensajes tipo CHAT)
    private String sender;     // Nombre del usuario que envía el mensaje
    private MessageType type;  // Tipo de mensaje: CHAT, JOIN o LEAVE

}
