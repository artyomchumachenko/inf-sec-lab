package ru.mai.is.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
}
