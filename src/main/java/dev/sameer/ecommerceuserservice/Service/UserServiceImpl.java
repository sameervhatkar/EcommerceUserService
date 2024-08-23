package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.*;
import dev.sameer.ecommerceuserservice.Entity.Role;
import dev.sameer.ecommerceuserservice.Entity.User;
import dev.sameer.ecommerceuserservice.Exception.UserNotFoundException;
import dev.sameer.ecommerceuserservice.Repository.RoleRepository;
import dev.sameer.ecommerceuserservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Override
    public UserResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setName(signInRequestDTO.getUserName());
        user.setEmail(signInRequestDTO.getUserEmail());
        user.setPassword(passwordEncoder.encode(signInRequestDTO.getUserPassword()));
        Role role = roleRepository.findRoleByRole(signInRequestDTO.getUserRole()).orElseGet(
                () -> roleRepository.save(new Role(signInRequestDTO.getUserRole()))
        );
        user.setRoles(List.of(role));
        userRepository.save(user);
        return UserResponseDTO.convertUserEntityToUserDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = userRepository.findByEmail(userUpdateRequestDTO.getUserEmail()).orElseThrow(
                    () -> new UserNotFoundException("User not found")
            );
            user.setName(userUpdateRequestDTO.getUserName());
            user.setEmail(userUpdateRequestDTO.getUserEmail());
            if (!encoder.matches(userUpdateRequestDTO.getUserPassword(), user.getPassword())) {
                user.setPassword(encoder.encode(userUpdateRequestDTO.getUserPassword()));
            }
            String roleName = userUpdateRequestDTO.getUserRole();
            Role role = roleRepository.findRoleByRole(roleName.toUpperCase()).orElseGet(
                    () ->roleRepository.save(new Role(roleName.toUpperCase()))
            );
            List<Role> roleList = user.getRoles();
            roleList.add(role);
            user.setRoles(roleList);
            userRepository.save(user);
            return UserResponseDTO.convertUserEntityToUserDTO(user);

    }

    @Override
    public UserResponseDTO logIn(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email " + loginRequestDTO.getEmail() + " not found")
        );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        if(authentication.isAuthenticated()) {
            user.setToken(jwtService.generateToken(loginRequestDTO.getEmail()));
            userRepository.save(user);
        }
        return UserResponseDTO.convertUserEntityToUserDTO(user);
    }

    @Override
    public boolean logout(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );
        user.setToken(null);
        userRepository.save(user);
        return true;
    }



    @Override
    public UserResponseDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId).get();
        return UserResponseDTO.convertUserEntityToUserDTO(user);
    }

    @Override
    public List<UserResponseDTO> getUser() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for(User user : users)
            userResponseDTOS.add(UserResponseDTO.convertUserEntityToUserDTO(user));
        return userResponseDTOS;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        userRepository.delete(user);
        return true;
    }

    @Override
    public UserClientResponseDTO extractUserId(String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).get();
        return UserClientResponseDTO.convertUserToUserClientResponseDTO(user);
    }
}
