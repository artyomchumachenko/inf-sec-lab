package ru.mai.is.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.algorithm.BlockRequest;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.service.algorithm.BlockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encrypt(@RequestBody BlockRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            return ResponseEntity.ok(blockService.encrypt(request, authorizationHeader));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new TextResponse("Block cipher encrypt error."));
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decrypt(@RequestBody BlockRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            return ResponseEntity.ok(blockService.decrypt(request, authorizationHeader));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new TextResponse("Block cipher decrypt error."));
        }
    }

    @GetMapping("/gost-key")
    public ResponseEntity<TextResponse> getGostKey() {
        return ResponseEntity.ok(new TextResponse(BlockService.DEFAULT_BLOCK_CIPHER_KEY));
    }

    @GetMapping("/last-key")
    public ResponseEntity<TextResponse> getLastKey(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(blockService.lastKey(authorizationHeader));
    }
}
