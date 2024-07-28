package dev.sameer.ecommerceuserservice.Controller;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import dev.sameer.ecommerceuserservice.DTO.UserUpdateRequestDTO;
import dev.sameer.ecommerceuserservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
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

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(userService.validate(authToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(userService.logout(authToken));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestHeader("Authorization") String authToken, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequestDTO, authToken));
    }

    @GetMapping("getUser/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("getUsers")
    public ResponseEntity<List<UserResponseDTO>> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @DeleteMapping("deleteUser/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }





}
