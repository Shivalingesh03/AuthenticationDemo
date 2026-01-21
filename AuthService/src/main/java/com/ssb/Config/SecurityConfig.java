package com.ssb.Config;

import com.ssb.Entity.Permissions;
import com.ssb.Entity.Role;
import com.ssb.Filter.JwtAuthFilter;
import com.ssb.Service.UserDetailsServiceImpl;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register","/auth/login").permitAll()
//                        .requestMatchers("/auth/weather").hasRole("USER")
//                        .requestMatchers("/auth/temperature").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/auth/**").hasAuthority(Permissions.DELETE.name())
                                .requestMatchers(HttpMethod.POST,"/auth/**").hasAuthority(Permissions.POST.name())
                                .requestMatchers(HttpMethod.PUT,"/auth/**").hasAuthority(Permissions.WRITE.name())
                                .requestMatchers(HttpMethod.GET,"/auth/**").hasAuthority(Permissions.READ.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
