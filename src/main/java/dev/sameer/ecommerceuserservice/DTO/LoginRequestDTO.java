package dev.sameer.ecommerceuserservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    String userEmail;
    private String userPassword;
}