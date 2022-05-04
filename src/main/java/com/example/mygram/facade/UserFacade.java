package com.example.mygram.facade;

import org.springframework.stereotype.Component;

import com.example.mygram.dto.UserDTO;
import com.example.mygram.entity.User;

/**
 * @author s.melekhin
 * @since 04 май 2022 г.
 */
@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        return userDTO;
    }

}
