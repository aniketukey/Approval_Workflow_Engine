package com.workflow.service;

import com.workflow.dto.LoginDto;

import com.workflow.dto.RegisterDto;

public interface AuthService {

	String login(LoginDto request);

	String register(RegisterDto request);

}
