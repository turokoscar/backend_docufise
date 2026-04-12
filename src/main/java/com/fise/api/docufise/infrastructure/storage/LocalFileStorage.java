package com.fise.api.docufise.infrastructure.storage;

import com.fise.api.docufise.domain.ports.output.IFileStoragePort;
import com.fise.api.docufise.infrastructure.config.FileStorageProperties;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;

@Component
public class LocalFileStorage implements IFileStoragePort {
    
    private final FileStorageProperties properties;
    private Path basePath;
    
    public LocalFileStorage(FileStorageProperties properties) {
        this.properties = properties;
    }
    
    @PostConstruct
    public void init() {
        this.basePath = Paths.get(properties.getBasePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento: " + basePath, e);
        }
    }
    
    @Override
    public String store(String filename, InputStream inputStream) {
        Path targetPath = basePath.resolve(filename).normalize();
        
        if (!targetPath.startsWith(basePath)) {
            throw new RuntimeException("Nombre de archivo inválido: " + filename);
        }
        
        try {
            // Crear subcarpetas si no existen
            Path parentDir = targetPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return filename; // Retornar solo el nombre relativo
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo: " + filename, e);
        }
    }
    
    @Override
    public boolean replace(String filename, InputStream inputStream) {
        Path targetPath = basePath.resolve(filename).normalize();
        
        if (!targetPath.startsWith(basePath)) {
            throw new RuntimeException("Nombre de archivo inválido: " + filename);
        }
        
        try {
            // Crear subcarpetas si no existen
            Path parentDir = targetPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error al reemplazar archivo: " + filename, e);
        }
    }
    
    @Override
    public boolean delete(String filename) {
        Path targetPath = basePath.resolve(filename).normalize();
        
        if (!targetPath.startsWith(basePath)) {
            throw new RuntimeException("Nombre de archivo inválido: " + filename);
        }
        
        try {
            return Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar archivo: " + filename, e);
        }
    }
    
    @Override
    public InputStream load(String filename) {
        Path targetPath = basePath.resolve(filename).normalize();
        
        if (!targetPath.startsWith(basePath)) {
            throw new RuntimeException("Nombre de archivo inválido: " + filename);
        }
        
        try {
            return new FileInputStream(targetPath.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo no encontrado: " + filename, e);
        }
    }
    
    @Override
    public String getFullPath(String filename) {
        return basePath.resolve(filename).normalize().toString();
    }
    
    @Override
    public boolean exists(String filename) {
        Path targetPath = basePath.resolve(filename).normalize();
        return Files.exists(targetPath);
    }
}