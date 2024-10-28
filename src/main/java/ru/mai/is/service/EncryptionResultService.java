package ru.mai.is.service;

import org.springframework.stereotype.Service;

import ru.mai.is.model.BlockCipherKey;
import ru.mai.is.model.EncryptionResult;
import ru.mai.is.model.RSAKey;
import ru.mai.is.repository.EncryptionResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionResultService {

    private final PdfGeneratorService pdfGeneratorService;
    private final EncryptionResultRepository encryptionResultRepository;

    public void saveEncryptionResult(BlockCipherKey blockCipherKey, String inputText, String resultText) {
        log.info("Saving results for: {}, {}", inputText, resultText);
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        //todo signature for pdfs
        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                resultTextPdf,
                null, // todo Impl signature logic
                null,
                blockCipherKey
        );
        encryptionResultRepository.save(encryptionResult);
    }

    public void saveRsaEncryptionResult(RSAKey key, String inputText, String resultText) {
        log.info("Saving results for: {}, {}", inputText, resultText);
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        //todo signature for pdfs
        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                resultTextPdf,
                null, // todo Impl signature logic
                key,
                null
        );
        encryptionResultRepository.save(encryptionResult);
    }
}
