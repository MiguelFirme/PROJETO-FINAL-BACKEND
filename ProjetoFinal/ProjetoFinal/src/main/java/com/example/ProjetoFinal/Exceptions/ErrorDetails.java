package com.example.ProjetoFinal.Exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private int status;
    private Map<String, String> validationErrors;

    public ErrorDetails(LocalDateTime timestamp, String message, String details, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status = status;
    }

    public ErrorDetails(LocalDateTime timestamp, String message, String details, int status, Map<String, String> validationErrors) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status = status;
        this.validationErrors = validationErrors;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public int getStatus() { return status; }
    public Map<String, String> getValidationErrors() { return validationErrors; }
}
