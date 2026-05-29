package com.raunak.backend.service;

import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private  UserRepository userRepository;

    public UserService( UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
