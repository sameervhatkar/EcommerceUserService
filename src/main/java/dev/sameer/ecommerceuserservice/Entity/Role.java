package dev.sameer.ecommerceuserservice.Entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "ECOM_ROLE")
public class Role extends BaseModel {
    private String role;

    public Role(String role) {
        this.role = role;
    }

    public Role() {
    }
}
