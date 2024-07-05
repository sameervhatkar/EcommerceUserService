package dev.sameer.ecommerceuserservice.Controller;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import dev.sameer.ecommerceuserservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(userService.signIn(signInRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> logIn(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(userService.logIn(loginRequestDTO));
    }

}
