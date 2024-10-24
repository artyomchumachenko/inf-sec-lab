package ru.mai.is.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.algorithm.RsaRequest;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.service.algorithm.RsaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rsa")
public class RsaController {

    private final RsaService rsaService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encrypt(@RequestBody RsaRequest request) {
        if (!rsaService.keysExist()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Keys not generated. Call /rsa/keys-generate.");
        }

        String encryptedText = rsaService.encrypt(request.getText());
        TextResponse response = new TextResponse(encryptedText);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decrypt(@RequestBody RsaRequest request) {
        if (!rsaService.keysExist()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Keys not generated. Call /rsa/keys-generate.");
        }

        String encryptedText = rsaService.decrypt(request.getText());
        TextResponse response = new TextResponse(encryptedText);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/keys-generate")
    public ResponseEntity<?> generateKeyPairs() {
        if (rsaService.keysExist()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Keys are already generated. Firstly delete old keys.");
        }

        rsaService.generateKeysAsync();
        return ResponseEntity.ok("Key pair generation began");
    }

    @GetMapping("/keys-status")
    public ResponseEntity<?> checkKeyPairGenerationStatus() {
        if (rsaService.keysExist()) {
            return ResponseEntity.ok("Keys generated. RSA ready to encrypt.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Keys not generated. Call /rsa/keys-generate.");
        }
    }
}
