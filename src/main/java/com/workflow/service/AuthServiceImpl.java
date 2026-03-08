package com.workflow.service;

import org.springframework.stereotype.Service;

import com.workflow.dto.LoginDto;
import com.workflow.dto.RegisterDto;
import com.workflow.entity.User;
import com.workflow.entity.enums.Role;
import com.workflow.repository.UserRepo;
import com.workflow.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto request) {
        User user = userRepo
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public String register(RegisterDto request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Default role to REQUESTER if not provided
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.REQUESTER);
        }

        userRepo.save(user);

        return "User registered successfully";
    }

}
