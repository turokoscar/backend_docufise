package com.fise.api.docufise.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DOCUFISE API")
                        .version("1.0.0")
                        .description("""
                            API REST para el sistema de gestión documental DOCUFISE.
                            
                            ## Autenticación
                            1. Llamar a `/api/v1/auth/login` con credenciales
                            2. Usar el token retornado en el header: `Authorization: Bearer <token>`
                            
                            ## Versionado
                            Se usa versionado en URL: `/api/v1/*`
                            
                            ## Códigos de Respuesta
                            - 200: Operación exitosa
                            - 400: Solicitud incorrecta
                            - 401: No autorizado
                            - 403: Acceso denegado
                            - 404: Recurso no encontrado
                            - 500: Error interno
                            """)
                        .contact(new Contact()
                                .name("Equipo FISE")
                                .email("fise@documentos.gob")
                                .url("https://www.fise.gob")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                    JWT Token Bearer
                                    
                                    Para obtener un token:
                                    ```bash
                                    curl -X POST http://localhost:8080/api/v1/auth/login \
                                      -H "Content-Type: application/json" \
                                      -d '{"nombreUsuario": "admin", "contrasena": "password"}'
                                    ```
                                    
                                    Luego usar el token en las solicitudes:
                                    ```bash
                                    curl http://localhost:8080/api/v1/documentos \
                                      -H "Authorization: Bearer <token>"
                                    ```
                                    """)));
    }
}