package com.raunak.backend.controller;

import com.raunak.backend.enums.SkillSignal;
import com.raunak.backend.model.Event;
import com.raunak.backend.model.User;
import com.raunak.backend.service.SkillsService;
import com.raunak.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
private  UserService userService;
    private SkillsService skillsService;

public UserController(UserService userService,SkillsService skillsService){
    this.userService=userService;
    this.skillsService=skillsService;
}
@PostMapping
    public User createUser(@RequestBody User user){
    return userService.saveUser(user);
}

@GetMapping
    public List<User> getUsers(){
    return userService.getAllUsers();
}


    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }



    @GetMapping("/{id}/events")
    public List<Event> getUserEvents(@PathVariable int id){
    return userService.getUserEvents(id);
    }

    @GetMapping("test")
    public void runthing (){
    skillsService.applySignal(
            4,
            "arrays",
            SkillSignal.MISTAKE
    );
}
}



