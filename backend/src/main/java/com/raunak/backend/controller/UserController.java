package com.raunak.backend.controller;

import com.raunak.backend.model.User;
import com.raunak.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
private  UserService userService;

public UserController(UserService userService){
    this.userService=userService;
}
@PostMapping
    public User createUser(@RequestBody User user){
    return userService.saveUser(user);
}

@GetMapping
    public List<User> getUsers(){
    return userService.getAllUsers();
}
}
