package com.fise.api.docufise.domain.model;

public enum Permiso {
    LECTURA("LECTURA", "Solo lectura"),
    ESCRITURA("ESCRITURA", "Lectura y escritura");

    private final String codigo;
    private final String descripcion;

    Permiso(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
}
