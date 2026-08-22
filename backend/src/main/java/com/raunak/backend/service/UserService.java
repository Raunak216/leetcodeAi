package com.raunak.backend.service;

import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository =
                userRepository;
    }

    public User saveUser(
            User user
    ) {

        return userRepository.save(
                user
        );
    }
}