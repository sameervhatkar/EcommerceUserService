package dev.sameer.ecommerceuserservice.Repository;

import dev.sameer.ecommerceuserservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByUserEmail(String userEmail);
}
