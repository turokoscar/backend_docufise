package com.fise.api.docufise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ApiDocufiseApplication {
    public static void main(String[] args) {
        // Cargar .env si existe para desarrollo local
        if (Files.exists(Path.of(".env"))) {
            Dotenv dotenv = Dotenv.configure().load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
        }
        
        SpringApplication.run(ApiDocufiseApplication.class, args);
    }
}
