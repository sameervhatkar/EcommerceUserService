package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import dev.sameer.ecommerceuserservice.Entity.Role;
import dev.sameer.ecommerceuserservice.Entity.User;
import dev.sameer.ecommerceuserservice.Exception.InvalidCredential;
import dev.sameer.ecommerceuserservice.Exception.UserNotFoundException;
import dev.sameer.ecommerceuserservice.Repository.RoleRepository;
import dev.sameer.ecommerceuserservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setUserName(signInRequestDTO.getUserName());
        user.setUserEmail(signInRequestDTO.getUserEmail());
        user.setUserPassword(encoder.encode(signInRequestDTO.getUserPassword()));
        Role role = roleRepository.findRoleByUserRole(signInRequestDTO.getUserRole());
        if(role == null) {
            role = new Role();
            role.setUserRole(signInRequestDTO.getUserRole());
            roleRepository.save(role);
        }
        user.setUserRoles(List.of(role));
        userRepository.save(user);
        return UserResponseDTO.convertUserEntityToUserDTO(user);
    }

    @Override
    public UserResponseDTO logIn(LoginRequestDTO loginRequestDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findUserByUserEmail(loginRequestDTO.getUserEmail());
        if(user == null) {
            throw new UserNotFoundException("User with the Email Id: " + loginRequestDTO.getUserEmail() + " not found");
        }
        if(encoder.matches(loginRequestDTO.getUserPassword(), user.getUserPassword())) {
            //Generate the Token
            String token = encoder.encode(user.getUserName() + user.getUserEmail() + LocalDateTime.now());
            user.setToken(token);
        }
        else
            throw new InvalidCredential("User Email or Password are incorrect");
        userRepository.save(user);
        return UserResponseDTO.convertUserEntityToUserDTO(user);
    }

    @Override
    public boolean validate(String token) {
        return false;
    }
}
