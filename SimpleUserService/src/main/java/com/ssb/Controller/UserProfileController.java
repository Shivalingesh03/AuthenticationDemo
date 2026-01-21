package com.ssb.Controller;

import com.ssb.DTO.UpdateProfileRequest;
import com.ssb.Entity.User;
import com.ssb.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // GET user profile
    @GetMapping("/getProfile")
    public User getProfile(Authentication authentication) {
        String username = authentication.getName();
        return userProfileService.getUserByUsername(username);
    }

    // POST/PUT update profile
    @PutMapping("/update")
    public User updateProfile(@RequestBody UpdateProfileRequest request,
                              Authentication authentication) {
        String username = authentication.getName();
        return userProfileService.updateUserProfile(
                username, request.getFirstName(), request.getLastName(), request.getEmail()
        );
    }


}
