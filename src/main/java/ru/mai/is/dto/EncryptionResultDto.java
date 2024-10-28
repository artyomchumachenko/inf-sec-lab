package ru.mai.is.dto;

import java.util.Base64;

import ru.mai.is.model.EncryptionResult;

import lombok.Data;

@Data
public class EncryptionResultDto {
    private String originalPdfBase64;
    private String resultPdfBase64;
    private String date;
    private String encryptionMethod;

    public EncryptionResultDto(EncryptionResult result) {
        this.originalPdfBase64 = Base64.getEncoder().encodeToString(result.getOriginalMessagePdf());
        this.resultPdfBase64 = Base64.getEncoder().encodeToString(result.getResultPdf());
        this.date = result.getCreatedAt().toString();
        this.encryptionMethod = result.getBlockCipherKey() == null ? "RSA" : "BLOCK";
    }
}
