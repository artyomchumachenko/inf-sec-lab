package ru.mai.is.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.dto.request.user.RegistrationRequest;
import ru.mai.is.model.User;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    @GetMapping("")
    public ResponseEntity<String> history() {
        return ResponseEntity.badRequest().body("Not implemented history logic on the server");
    }
}
