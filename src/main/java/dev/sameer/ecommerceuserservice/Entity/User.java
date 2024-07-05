package dev.sameer.ecommerceuserservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel {
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
