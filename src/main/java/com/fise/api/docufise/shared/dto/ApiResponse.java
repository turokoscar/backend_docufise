package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estructura estandarizada para todas las respuestas de la API institucional.
 * Adaptado de los estándares MIDAGRI/SEGDI y el patrón .NET proporcionado.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    
    private Boolean exitoso;
    private Integer respuesta; // 1 para éxito, 2 para error, 3 para no encontrado
    private Integer codigo;    // Código HTTP o código interno
    private String mensaje;
    private T datos;
    private Object total;

    public static <T> ApiResponse<T> ok(T datos) {
        return ApiResponse.<T>builder()
                .exitoso(true)
                .respuesta(1)
                .mensaje("Operación exitosa")
                .datos(datos)
                .build();
    }

    public static <T> ApiResponse<T> ok(T datos, String mensaje) {
        return ApiResponse.<T>builder()
                .exitoso(true)
                .respuesta(1)
                .mensaje(mensaje)
                .datos(datos)
                .build();
    }

    public static <T> ApiResponse<T> ok(T datos, String mensaje, Object total) {
        return ApiResponse.<T>builder()
                .exitoso(true)
                .respuesta(1)
                .mensaje(mensaje)
                .datos(datos)
                .total(total)
                .build();
    }

    public static <T> ApiResponse<T> error(String mensaje) {
        return ApiResponse.<T>builder()
                .exitoso(false)
                .respuesta(2)
                .mensaje(mensaje)
                .build();
    }

    public static <T> ApiResponse<T> error(String mensaje, Integer codigo) {
        return ApiResponse.<T>builder()
                .exitoso(false)
                .respuesta(2)
                .codigo(codigo)
                .mensaje(mensaje)
                .build();
    }

    public static <T> ApiResponse<T> notFound(String mensaje) {
        return ApiResponse.<T>builder()
                .exitoso(false)
                .respuesta(3)
                .codigo(404)
                .mensaje(mensaje)
                .build();
    }
}
