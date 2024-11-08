package ru.mai.is.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.hash.EncodeRequest;
import ru.mai.is.dto.request.hash.HashRequest;
import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.dto.request.user.RegistrationRequest;
import ru.mai.is.model.User;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<User> registrationUser(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(userService.registration(request));
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
        }
    }

    @PostMapping("/hash")
    public ResponseEntity<String> getStribogHash(@RequestBody HashRequest hashRequest) {
        String hash = userService.getStribogHash(hashRequest);
        return ResponseEntity.ok(hash);
    }

    @PostMapping("/encode")
    public ResponseEntity<String> encodePassword(@RequestBody EncodeRequest encodeRequest) {
        String encodedPassword = userService.encodePassword(encodeRequest);
        return ResponseEntity.ok(encodedPassword);
    }
}
