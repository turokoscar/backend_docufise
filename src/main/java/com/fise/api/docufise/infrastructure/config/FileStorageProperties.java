package com.fise.api.docufise.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {
    
    private String type = "local";
    private String basePath = "./uploads";
    private String[] allowedExtensions = {"pdf", "doc", "docx", "xls", "xlsx", "png", "jpg", "jpeg"};
    private long maxSize = 52428800;
    
    private SftpProperties sftp = new SftpProperties();
    private S3Properties s3 = new S3Properties();
    
    @Data
    public static class SftpProperties {
        private String host = "localhost";
        private int port = 22;
        private String username = "";
        private String password = "";
        private String remotePath = "/uploads/docufise";
        private String privateKeyPath = "";
    }
    
    @Data
    public static class S3Properties {
        private String bucket = "";
        private String region = "us-east-1";
        private String accessKey = "";
        private String secretKey = "";
        private String endpoint = "";
    }
}