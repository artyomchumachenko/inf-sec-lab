package ru.mai.is.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.BlockRequest;
import ru.mai.is.dto.response.ErrorResponse;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.service.BlockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encrypt(@RequestBody BlockRequest request) {
        try {
            String encryptedText = blockService.encrypt(request.getText(), request.getMode());
            TextResponse response = new TextResponse(encryptedText);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("Block cipher encrypt error."));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decrypt(@RequestBody BlockRequest request) {
        try {
            String decryptText = blockService.decrypt(request.getText(), request.getMode());
            TextResponse response = new TextResponse(decryptText);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("Block cipher decrypt error."));
        }
    }
}
