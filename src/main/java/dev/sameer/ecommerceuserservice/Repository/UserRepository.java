package dev.sameer.ecommerceuserservice.Repository;

import dev.sameer.ecommerceuserservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String userEmail);
    Optional<User> findByToken(String token);
}
