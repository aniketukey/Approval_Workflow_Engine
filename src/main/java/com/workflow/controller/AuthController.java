package com.workflow.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.dto.LoginDto;
import com.workflow.dto.RegisterDto;
import com.workflow.repository.UserRepo;
import com.workflow.security.JwtUtil;
import com.workflow.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final AuthService authService;

    // @PreAuthorize("hasAnyRole('ADMIN' , 'REQUESTER' , 'APPROVER')")
    @PostMapping("/login")
    public String login(@RequestBody LoginDto request) {

        return authService.login(request);

    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDto request) {
        return authService.register(request);
    }
}