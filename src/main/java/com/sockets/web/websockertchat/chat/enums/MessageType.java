package com.sockets.web.websockertchat.chat.enums;

/**
 * Define los tres tipos de mensajes que pueden circular por el chat.
 *
 * El frontend usa este campo "type" para saber cómo renderizar cada mensaje:
 *   - CHAT  → muestra el avatar del usuario + su nombre + el contenido del mensaje
 *   - JOIN  → muestra un mensaje de evento: "X joined!"  (sin avatar)
 *   - LEAVE → muestra un mensaje de evento: "X left!"    (sin avatar)
 *
 * El tipo LEAVE no lo envía el cliente explícitamente — lo genera el servidor
 * en WebSockertEventListener cuando detecta que la sesión WebSocket se cerró.
 */
public enum MessageType {

    CHAT,   // Mensaje de texto normal enviado por un usuario
    JOIN,   // Notificación de que un usuario se unió al chat
    LEAVE   // Notificación de que un usuario abandonó el chat

}
