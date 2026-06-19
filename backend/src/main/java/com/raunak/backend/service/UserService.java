package com.raunak.backend.service;

import com.raunak.backend.model.Event;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private  UserRepository userRepository;
    private final SkillsService skillsService;
    public UserService(
            UserRepository userRepository,
            SkillsService skillsService
    ){
        this.userRepository =
                userRepository;

        this.skillsService =
                skillsService;
    }
    public User saveUser(
            User user
    ){
        User savedUser =
                userRepository.save(
                        user
                );

        skillsService
                .getOrCreateSkills(
                        savedUser
                );

        return savedUser;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public List<Event> getUserEvents(int userId){
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        return user.getEvents();
    }

}
