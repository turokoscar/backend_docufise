package com.fise.api.docufise.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String filename;
    private String filepath;
    private long size;
    private String contentType;
    private String message;
}