package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import dev.sameer.ecommerceuserservice.DTO.UserUpdateRequestDTO;
import dev.sameer.ecommerceuserservice.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    UserResponseDTO signIn(SignInRequestDTO signInRequestDTO);
    UserResponseDTO logIn(LoginRequestDTO loginRequestDTO);
    boolean validate(String token);
    boolean logout(String token);
    UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO, String token);
    UserResponseDTO getUserById(UUID userId);
    List<UserResponseDTO> getUser();
    boolean deleteUser(UUID userId);
    User getUserByUserEmail(String userEmail);
}
