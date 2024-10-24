package ru.mai.is.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.algorithm.StribogRequest;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.service.algorithm.StribogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stribog")
public class StribogController {

    private final StribogService stribogService;

    @PostMapping("/hash")
    public ResponseEntity<TextResponse> stribog(@RequestBody StribogRequest stribogRequest) {
        TextResponse textResponse = new TextResponse(stribogService.getHash(stribogRequest));
        return ResponseEntity.ok(textResponse);
    }
}
