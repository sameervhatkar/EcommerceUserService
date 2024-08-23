package dev.sameer.ecommerceuserservice.Controller;

import dev.sameer.ecommerceuserservice.DTO.*;
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


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(userService.signIn(signInRequestDTO));
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> logIn(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(userService.logIn(loginRequestDTO));
    }

    //Using to 3rd Party Service (Product, Order, Payment)
    @GetMapping("/authenticate")
    public ResponseEntity<Boolean> validate() {
        return ResponseEntity.ok(true);
    }
    //Here we are passing the token in endpoints and in the arguments
    //In the endpoints because we want to validate the token
    @PostMapping( "/logout/{token}")
    public ResponseEntity<Boolean> logout(@PathVariable String token) {
        return ResponseEntity.ok(userService.logout(token));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequestDTO));
    }

    @GetMapping("getUser/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@RequestHeader("Authorization") String authToken, @PathVariable UUID userId) {
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

    @GetMapping("/extractUser")
    public ResponseEntity<UserClientResponseDTO> extractUserId(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.extractUserId(token));
    }

}
