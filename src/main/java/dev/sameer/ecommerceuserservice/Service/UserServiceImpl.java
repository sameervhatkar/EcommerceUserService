package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.DTO.LoginRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.SignInRequestDTO;
import dev.sameer.ecommerceuserservice.DTO.UserResponseDTO;
import dev.sameer.ecommerceuserservice.DTO.UserUpdateRequestDTO;
import dev.sameer.ecommerceuserservice.Entity.Role;
import dev.sameer.ecommerceuserservice.Entity.User;
import dev.sameer.ecommerceuserservice.Exception.InvalidCredential;
import dev.sameer.ecommerceuserservice.Exception.InvalidTokenException;
import dev.sameer.ecommerceuserservice.Exception.UserNotFoundException;
import dev.sameer.ecommerceuserservice.Repository.RoleRepository;
import dev.sameer.ecommerceuserservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO, String token) {
        if(validate(token))
        {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user = userRepository.findUserByUserEmail(userUpdateRequestDTO.getUserEmail()).orElseThrow(
                    () -> new UserNotFoundException("User not found")
            );
            user.setUserName(userUpdateRequestDTO.getUserName());
            user.setUserEmail(userUpdateRequestDTO.getUserEmail());
            if (!encoder.matches(userUpdateRequestDTO.getUserPassword(), user.getUserPassword())) {
                user.setUserPassword(encoder.encode(userUpdateRequestDTO.getUserPassword()));
            }
            Role role = roleRepository.findRoleByUserRole(userUpdateRequestDTO.getUserRole());
            if (role == null) {
                role = new Role();
                role.setUserRole(userUpdateRequestDTO.getUserRole());
                roleRepository.save(role);
            }
            List<Role> roleList = user.getUserRoles();
            roleList.add(role);
            user.setUserRoles(roleList);
            userRepository.save(user);
            return UserResponseDTO.convertUserEntityToUserDTO(user);
        }
        return null;
    }

    @Override
    public UserResponseDTO logIn(LoginRequestDTO loginRequestDTO) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findUserByUserEmail(loginRequestDTO.getUserEmail()).orElseThrow(
                () -> new UserNotFoundException("User with the Email Id: " + loginRequestDTO.getUserEmail() + " not found")
        );

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
        User user = userRepository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("Token is invalid")
        );
        return true;
    }

    @Override
    public boolean logout(String token) {
        User user = userRepository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("Token is invalid")
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
    public User getUserByUserEmail(String userEmail) {
        User user = userRepository.findUserByUserEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return user;
    }
}
