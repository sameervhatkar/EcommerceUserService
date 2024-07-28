package dev.sameer.ecommerceuserservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userRole;
}
