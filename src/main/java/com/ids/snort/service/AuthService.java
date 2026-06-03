package com.ids.snort.service;

import com.ids.snort.dto.AuthResponse;
import com.ids.snort.dto.LoginRequest;
import com.ids.snort.dto.RegisterRequest;
import com.ids.snort.model.Role;
import com.ids.snort.model.User;
import com.ids.snort.repository.UserRepository;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  public AuthResponse register(RegisterRequest request) {
    userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
      throw new IllegalArgumentException("Email already registered");
    });

    User user = new User();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail().toLowerCase());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    if (userRepository.count() == 0) {
      user.setRole(Role.ADMIN);
    } else {
      user.setRole(Role.ANALYST);
    }

    userRepository.save(user);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    String token = jwtService.generateToken(userDetails, Map.of("role", user.getRole().name()));
    return new AuthResponse(token, user.getRole().name(), user.getFullName());
  }

  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail().toLowerCase())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    String token = jwtService.generateToken(userDetails, Map.of("role", user.getRole().name()));
    return new AuthResponse(token, user.getRole().name(), user.getFullName());
  }
}
