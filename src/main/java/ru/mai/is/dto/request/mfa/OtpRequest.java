package ru.mai.is.dto.request.mfa;

import lombok.Data;

@Data
public class OtpRequest {
    private String username;
    private String otp;
}
