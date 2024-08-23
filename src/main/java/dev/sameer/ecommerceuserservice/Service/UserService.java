package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.*;
import dev.sameer.ecommerceuserservice.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    UserResponseDTO signIn(SignInRequestDTO signInRequestDTO);
    UserResponseDTO logIn(LoginRequestDTO loginRequestDTO);


    boolean logout(String token);
    UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO);
    UserResponseDTO getUserById(UUID userId);
    List<UserResponseDTO> getUser();
    boolean deleteUser(UUID userId);

    UserClientResponseDTO extractUserId(String token);
}
