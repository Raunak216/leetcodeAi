package com.raunak.backend.controller;

import com.raunak.backend.dto.LeetcodeProfileRequest;
import com.raunak.backend.model.LeetcodeProfile;
import com.raunak.backend.service.EventService;
import com.raunak.backend.service.LeetcodeProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class LeetcodeProfileController {
    public final LeetcodeProfileService leetcodeProfileService;

    public LeetcodeProfileController(LeetcodeProfileService leetcodeProfileService) {
        this.leetcodeProfileService = leetcodeProfileService;
    }

    @PostMapping
    public LeetcodeProfile createProfile(@Valid @RequestBody LeetcodeProfileRequest request){

        return leetcodeProfileService.saveProfile(request);
    }

    @GetMapping("/{userId}")
    public LeetcodeProfile getLeetcodeProfile(@PathVariable int userId){
        return leetcodeProfileService.getLeetcodeProfile(userId);
    }

}
