package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDTO signIn(SignInRequestDTO signInRequestDTO);
    UserResponseDTO logIn(LoginRequestDTO loginRequestDTO);
    boolean validate(String token);

}
