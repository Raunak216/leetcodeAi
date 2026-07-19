package com.raunak.backend.service;

import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final SkillProfileService skillProfileService;
    private UserRepository userRepository;

    public UserService(
            UserRepository userRepository,
            SkillProfileService skillProfileService
    ) {
        this.userRepository =
                userRepository;

        this.skillProfileService =
                skillProfileService;
    }

    public User saveUser(
            User user
    ) {
        User savedUser =
                userRepository.save(
                        user
                );

        skillProfileService.getOrCreateProfile(
                savedUser
        );

        return savedUser;
    }
}
