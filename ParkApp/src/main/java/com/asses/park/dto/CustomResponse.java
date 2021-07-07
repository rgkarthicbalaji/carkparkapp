package com.asses.park.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class CustomResponse {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    private CustomResponse() {
        timestamp = LocalDateTime.now();
    }

    public CustomResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = ex.getMessage();
    }

    public CustomResponse(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
