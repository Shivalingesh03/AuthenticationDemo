package com.ssb.Service;


import com.ssb.DTO.AuthRequest;
import com.ssb.DTO.RegisterRequestDto;
import com.ssb.Entity.Role;
import com.ssb.Entity.User;
import com.ssb.Repository.UserRepository;
import com.ssb.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Registration
    public String register(RegisterRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Set<Role> roles = dto.getRoles().stream()
                .map(role -> Role.valueOf(role.toUpperCase()))
                .collect(Collectors.toSet());

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(roles);
        user.setEnabled(true);

        userRepository.save(user);
        return "User registered successfully";
    }

    // Login
    public String login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            return jwtUtil.generateToken(authRequest.getUsername());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password");
        }
    }
}
