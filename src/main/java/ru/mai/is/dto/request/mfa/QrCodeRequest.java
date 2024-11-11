package ru.mai.is.dto.request.mfa;

import lombok.Data;

@Data
public class QrCodeRequest {
    private String username;
    private String code;
}
