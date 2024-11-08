package ru.mai.is.dto.request.hash;

import lombok.Data;

@Data
public class EncodeRequest {
    private String username;
    private String password;
}
