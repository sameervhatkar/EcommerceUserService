package dev.sameer.ecommerceuserservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    @Getter
    private String userName;
    private String userEmail;
    private String userPassword;
    private String token;
    @ManyToMany
    private List<Role> userRoles;


}

/*
    User        Role
    1           M
    M            1
 */
