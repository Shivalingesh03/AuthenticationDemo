package com.ssb.Controller;

import com.ssb.DTO.AuthRequest;
import com.ssb.DTO.RegisterRequestDto;
import com.ssb.Entity.Role;
import com.ssb.Entity.User;
import com.ssb.Repository.UserRepository;
import com.ssb.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


        @PostMapping("/register")
        public String register(
                @RequestBody RegisterRequestDto registerRequestDto) {
            // roles = ROLE_USER, ROLE_ADMIN

            if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            Set<Role> roles = registerRequestDto.getRoles().stream()
                    .map(role -> Role.valueOf(role.toUpperCase()))
                    .collect(Collectors.toSet());

            User user = new User();
            user.setUsername(registerRequestDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

            user.setRoles(roles);   // ✔ correct

            user.setEnabled(true);

            userRepository.save(user);
            return "User registered successfully";
        }

    @PostMapping("/login")
    public String login(
            @RequestBody AuthRequest authrequest ){
          try{
              authenticationManager.authenticate(
                      new UsernamePasswordAuthenticationToken(authrequest.getUsername(),authrequest.getPassword())
              );
              return jwtUtil.generateToken(authrequest.getUsername());

          } catch (AuthenticationException e) {
              throw new RuntimeException(e);
          }

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );

    }


    @GetMapping("/weather")
    public ResponseEntity<?> weather(){
            return ResponseEntity.ok("Healthy");
    }

    @GetMapping("/temperature")
    public ResponseEntity<?> temprature(){
        return ResponseEntity.ok("Temperature is 27*C");
    }

    @PostMapping("/postWeather")
    public ResponseEntity<?> postWeather(){
        return ResponseEntity.ok("postWeather");
    }

    @PutMapping("/putWeather")
    public ResponseEntity<?> putWeather(){
        return ResponseEntity.ok("postWeather");
    }

    @DeleteMapping("/deleteWeather")
    public ResponseEntity<?> deleteWeather(){
        return ResponseEntity.ok("deleteWeather");
    }





}
