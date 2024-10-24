package ru.mai.is.dto.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
