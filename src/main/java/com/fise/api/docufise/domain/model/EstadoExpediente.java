package com.fise.api.docufise.domain.model;

public enum EstadoExpediente {
    REGISTRADO(1, "Registrado", "#3B7DCC"),
    INGRESADO(2, "Ingresado", "#2C5AAB"),
    PENDIENTE(3, "Pendiente", "#F2B801"),
    OBSERVADO(4, "Observado", "#AB2741"),
    FIRMADO(5, "Firmado", "#0FBF90");

    private final int id;
    private final String nombre;
    private final String colorHex;

    EstadoExpediente(int id, String nombre, String colorHex) {
        this.id = id;
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getColorHex() { return colorHex; }

    public static EstadoExpediente fromId(int id) {
        for (EstadoExpediente estado : values()) {
            if (estado.id == id) return estado;
        }
        throw new IllegalArgumentException("Estado desconocido: " + id);
    }
}
