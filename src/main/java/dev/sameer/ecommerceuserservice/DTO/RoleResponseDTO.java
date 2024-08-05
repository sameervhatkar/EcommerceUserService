package dev.sameer.ecommerceuserservice.DTO;

import dev.sameer.ecommerceuserservice.Entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDTO {
    private String role;

    public static RoleResponseDTO convertRoleEntityToRoleDTO(Role role) {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setRole(role.getRole());
        return roleResponseDTO;
    }
}
