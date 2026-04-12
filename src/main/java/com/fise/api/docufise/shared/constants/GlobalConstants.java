package com.fise.api.docufise.shared.constants;

/**
 * Constantes globales para el sistema DocuFISE.
 * Centraliza nombres de roles, estados y configuraciones de dominio.
 */
public final class GlobalConstants {

    private GlobalConstants() {
        // Constructor privado para evitar instanciación
    }

    // Prefijo de la API
    public static final String API_V1 = "/api/v1";

    // Roles de Usuario
    public static final String ROLE_ADMIN = "Administrador";
    public static final String ROLE_CTD = "CTD";
    public static final String ROLE_FIRMANTE = "Firmante";

    // Estados de Expediente
    public static final String ESTADO_REGISTRADO = "REGISTRADO";
    public static final String ESTADO_INGRESADO = "INGRESADO";
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_OBSERVADO = "OBSERVADO";
    public static final String ESTADO_FIRMADO = "FIRMADO";

    // Permisos
    public static final String PERMISO_LECTURA = "LECTURA";
    public static final String PERMISO_ESCRITURA = "ESCRITURA";
}
