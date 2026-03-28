package com.sportslot.service;

import com.sportslot.config.JwtUtil;
import com.sportslot.dto.request.LoginRequest;
import com.sportslot.dto.request.RegisterRequest;
import com.sportslot.entity.User;
import com.sportslot.exception.ResourceNotFoundException;
import com.sportslot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        // Use provided role or default to USER
        User.Role role = request.getRole() != null ? request.getRole() : User.Role.USER;

        // Prevent self-assigning ADMIN role
        if (role == User.Role.ADMIN) role = User.Role.USER;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(role)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of(
            "token", token,
            "message", "Registration successful",
            "role", role.name()
        );
    }

    public Map<String, String> login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of(
            "token", token,
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole().name()
        );
    }
}
