package com.example.mygram.service;

import com.example.mygram.entity.User;
import com.example.mygram.entity.enums.ERole;
import com.example.mygram.exceptions.UserExistException;
import com.example.mygram.payload.request.SignupRequest;
import com.example.mygram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setFirstname(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);
        try {
            LOGGER.info("Saving user {}", userIn.getUsername());
            return userRepository.save(user);
        } catch (Exception exception) {
            LOGGER.error("Error during registration. {}", exception.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

}
