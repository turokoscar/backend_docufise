package com.fise.api.docufise.domain.ports.output;

import java.io.InputStream;

public interface IFileStoragePort {
    
    String store(String filename, InputStream inputStream);
    
    boolean replace(String filename, InputStream inputStream);
    
    boolean delete(String filename);
    
    InputStream load(String filename);
    
    String getFullPath(String filename);
    
    boolean exists(String filename);
}