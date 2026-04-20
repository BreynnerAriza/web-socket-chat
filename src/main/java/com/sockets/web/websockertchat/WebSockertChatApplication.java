package com.sockets.web.websockertchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación.
 *
 * @SpringBootApplication activa el escaneo automático de componentes, la
 * configuración automática de Spring Boot y la configuración de beans.
 * Al ejecutar main(), Spring levanta el servidor embebido (Tomcat por defecto)
 * y registra todos los beans del proyecto, incluyendo la configuración
 * del WebSocket definida en WebSocketConfig.
 */
@SpringBootApplication
public class WebSockertChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSockertChatApplication.class, args);
    }

}
