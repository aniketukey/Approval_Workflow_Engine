package com.workflow.dto;

import com.workflow.entity.enums.Role;
import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private Role role; // Optional: user could provide role, or you might default it in service
}
