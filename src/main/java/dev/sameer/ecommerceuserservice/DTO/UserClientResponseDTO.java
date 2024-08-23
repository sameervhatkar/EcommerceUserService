package dev.sameer.ecommerceuserservice.DTO;

import lombok.Getter;
import lombok.Setter;
import dev.sameer.ecommerceuserservice.Entity.User;


import java.util.UUID;

@Getter
@Setter
public class UserClientResponseDTO {
    private UUID id;
    private String userName;
    private String userEmail;

    public UserClientResponseDTO(UUID id, String userName, String userEmail) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public static UserClientResponseDTO convertUserToUserClientResponseDTO(User user) {
        return new UserClientResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

    }
}
