package ru.mai.is.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.RegistrationRequest;
import ru.mai.is.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public void registrationUser(@RequestBody RegistrationRequest request) {
        userService.registrationUser(request);
    }
}
