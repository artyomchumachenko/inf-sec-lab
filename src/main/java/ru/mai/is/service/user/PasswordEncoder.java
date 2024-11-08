package ru.mai.is.service.user;

import ru.mai.is.dto.request.user.LoginRequest;

public interface PasswordEncoder {
    String encode(String username, String password);
    boolean matches(LoginRequest loginRequest, String expectedPassword);
}
