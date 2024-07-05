package dev.sameer.ecommerceuserservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExceptionResponseDTO {
    private String message;
    private int code;

    public ExceptionResponseDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
