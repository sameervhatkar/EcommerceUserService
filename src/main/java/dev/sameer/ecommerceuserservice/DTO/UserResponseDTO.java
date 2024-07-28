package dev.sameer.ecommerceuserservice.DTO;

import dev.sameer.ecommerceuserservice.Entity.Role;
import dev.sameer.ecommerceuserservice.Entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
    private String userName;
    private String userEmail;
    private List<RoleResponseDTO> roleResponseDTOList;

    public static UserResponseDTO convertUserEntityToUserDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setUserEmail(user.getUserEmail());
        List<Role> roleList = user.getUserRoles();
        List<RoleResponseDTO> roleResponseDTOS = new ArrayList<>();
        for(Role role : roleList)
            roleResponseDTOS.add(RoleResponseDTO.convertRoleEntityToRoleDTO(role));
        userResponseDTO.setRoleResponseDTOList(roleResponseDTOS);
        return userResponseDTO;
    }
}
