package com.raunak.backend.service;

import com.raunak.backend.model.LeetcodeProfile;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.LeetcodeProfileRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.raunak.backend.dto.LeetcodeProfileRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeetcodeProfileService {
    private LeetcodeProfileRepository leetcodeProfileRepository;
    private UserRepository userRepository;


    public LeetcodeProfileService(LeetcodeProfileRepository leetcodeProfileRepository,UserRepository userRepository){
        this.leetcodeProfileRepository=leetcodeProfileRepository;
        this.userRepository=userRepository;
    }

    public LeetcodeProfile saveProfile(LeetcodeProfileRequest request){

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        LeetcodeProfile profile = new LeetcodeProfile();

        profile.setLeetcodeUsername(request.getLeetcodeUsername());
        profile.setEasySolved(request.getEasySolved());
        profile.setMediumSolved(request.getMediumSolved());
        profile.setHardSolved(request.getHardSolved());
        profile.setContestRating(request.getContestRating());
        profile.setLastSyncedAt(LocalDateTime.now());
        profile.setUser(user);

        return leetcodeProfileRepository.save(profile);
    }
    public LeetcodeProfile getLeetcodeProfile(int userId){
        return leetcodeProfileRepository.findById(userId).orElseThrow(()->new RuntimeException("Leetcode profile not founde"));
    }
}
