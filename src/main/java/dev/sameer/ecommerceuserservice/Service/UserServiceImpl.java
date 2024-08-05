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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {


    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserServiceImpl(CustomUserDetailsServiceImpl customUserDetailsServiceImpl, UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }




    @Override
    public UserResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(signInRequestDTO.getUserPassword());
        List<Role> roleList = new ArrayList<>();
        String roleName = signInRequestDTO.getUserRole();
        Role role = roleRepository.findRoleByRole(roleName).orElseGet(
                () -> roleRepository.save(new Role(roleName.toUpperCase()))
        );
        var user = User.builder()
                .name(signInRequestDTO.getUserName())
                .email(signInRequestDTO.getUserEmail())
                .password(encodedPassword)
                .roles(List.of(role))
                .token(null)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isAccountNonExpired(true)
                .build();
        return UserResponseDTO.convertUserEntityToUserDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO, String token) {
        if(validate(token))
        {
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
        return null;
    }

    @Override
    public UserResponseDTO logIn(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).get();
            String token = jwtService.generateToken(loginRequestDTO.getEmail(), user.getRoles().get(0).getRole());
            user.setToken(token);
            return UserResponseDTO.convertUserEntityToUserDTO(userRepository.save(user));
        } catch (Exception ex) {
            throw new UserNotFoundException("Authentication failed");
        }
    }



    @Override
    public boolean validate(String token) {
        try {
            // Ensure the token starts with the expected prefix
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String userEmail = jwtService.extractUsername(jwtToken);

                // Load user details from the extracted username
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

                // Validate the token against the user details
                return jwtService.validateToken(jwtToken, userDetails);
            }
        } catch (MalformedJwtException e) {
            System.err.println("Malformed JWT: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
        }
        // Return false if token is invalid or any exception occurs
        return false;
    }

    @Override
    public boolean logout(String token) {
        String userEmail = "";
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            userEmail = jwtService.extractUsername(jwtToken);

            // Load user details from the extracted username
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // Validate the token against the user details
            if(jwtService.validateToken(jwtToken, userDetails)) {
                User user = userRepository.findByEmail(userEmail).get();
                user.setToken(null);
                userRepository.save(user);
                return true;
            }
        }

        return false;
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
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return user;
    }
}
