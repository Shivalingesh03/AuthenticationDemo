package com.ssb.Service;


import com.ssb.Entity.User;
import com.ssb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    // Get user by username
    public User getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    // Update user profile
    public User updateUserProfile(String username, String firstName, String lastName, String email) {
        User user = getUserByUsername(username);

        if (!email.equals(user.getEmail()) && userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        return userRepository.save(user);
    }
}
