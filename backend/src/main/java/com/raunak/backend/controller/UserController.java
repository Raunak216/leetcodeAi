package com.raunak.backend.controller;

import com.raunak.backend.enums.SkillSignal;
import com.raunak.backend.model.Event;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.service.SkillProfileService;
import com.raunak.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
private  UserService userService;
    private SkillProfileService skillProfileService;
    private UserRepository userRepository;
public UserController(UserService userService, SkillProfileService skillProfileService,UserRepository userRepository){
    this.userService=userService;
    this.skillProfileService = skillProfileService;
    this.userRepository=userRepository;
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

    @GetMapping("/test")
    public void runthing (){
        skillProfileService.getOrCreateProfile(
                userRepository.findById(3).orElseThrow()
        );
}
    @PostMapping("/init/{id}")
    public String init(@PathVariable int id){

        User user =
                userService.getUserById(id);

        skillProfileService.getOrCreateProfile(user);

        return "done";
    }


}



