package dev.sameer.ecommerceuserservice.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Ecom_user")
public class User extends BaseModel {
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;
    private String token;
}


/*
    User        Role
    1           1
    M            1
 */
