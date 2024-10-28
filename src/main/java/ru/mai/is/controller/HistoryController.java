package ru.mai.is.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.EncryptionResultDto;
import ru.mai.is.model.EncryptionResult;
import ru.mai.is.service.EncryptionResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final EncryptionResultService resultService;

    @GetMapping("")
    public ResponseEntity<List<EncryptionResultDto>> history(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        List<EncryptionResult> encryptionResults = resultService.findAllResults(authorizationHeader);

        List<EncryptionResultDto> historyDtos = encryptionResults.stream()
                .map(EncryptionResultDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(historyDtos);
    }
}
