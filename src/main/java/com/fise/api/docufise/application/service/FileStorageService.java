package com.fise.api.docufise.application.service;

import com.fise.api.docufise.domain.ports.output.IFileStoragePort;
import com.fise.api.docufise.infrastructure.config.FileStorageProperties;
import com.fise.api.docufise.infrastructure.storage.LocalFileStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final FileStorageProperties properties;
    private final LocalFileStorage localStorage;
    
    public FileStorageService(FileStorageProperties properties, LocalFileStorage localStorage) {
        this.properties = properties;
        this.localStorage = localStorage;
    }
    
    public String upload(MultipartFile file, String subFolder) throws IOException {
        validateFile(file);
        
        String extension = getExtension(file.getOriginalFilename());
        String filename = generateFilename(extension, subFolder);
        
        return localStorage.store(filename, file.getInputStream());
    }
    
    public boolean replace(String filename, MultipartFile file) throws IOException {
        validateFile(file);
        return localStorage.replace(filename, file.getInputStream());
    }
    
    public boolean delete(String filename) {
        return localStorage.delete(filename);
    }
    
    public InputStream download(String filename) {
        return localStorage.load(filename);
    }
    
    public byte[] getBytes(String filename) {
        try (InputStream is = localStorage.load(filename)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer archivo: " + filename, e);
        }
    }
    
    public boolean exists(String filename) {
        return localStorage.exists(filename);
    }
    
    public String getFullPath(String filename) {
        return localStorage.getFullPath(filename);
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }
        
        String extension = getExtension(originalFilename).toLowerCase();
        boolean allowed = false;
        for (String ext : properties.getAllowedExtensions()) {
            if (ext.equalsIgnoreCase(extension)) {
                allowed = true;
                break;
            }
        }
        
        if (!allowed) {
            throw new IllegalArgumentException("Tipo de archivo no permitido: " + extension);
        }
        
        if (file.getSize() > properties.getMaxSize()) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo de " + 
                (properties.getMaxSize() / 1024 / 1024) + "MB");
        }
    }
    
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    private String generateFilename(String extension, String subFolder) {
        String uuid = UUID.randomUUID().toString();
        if (subFolder != null && !subFolder.isBlank()) {
            return subFolder + "/" + uuid + "." + extension;
        }
        return uuid + "." + extension;
    }
}