package dev.sameer.ecommerceuserservice.Service;

import dev.sameer.ecommerceuserservice.Entity.MyUserDetails;
import dev.sameer.ecommerceuserservice.Entity.User;
import dev.sameer.ecommerceuserservice.Exception.UserNotFoundException;
import dev.sameer.ecommerceuserservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("User with email " + userEmail + " not found")
        );
        return new MyUserDetails(user);
    }
}
