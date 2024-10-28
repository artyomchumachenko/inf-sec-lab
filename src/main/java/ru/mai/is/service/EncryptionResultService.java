package ru.mai.is.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.mai.is.model.BlockCipherKey;
import ru.mai.is.model.EncryptionResult;
import ru.mai.is.model.RSAKey;
import ru.mai.is.model.User;
import ru.mai.is.repository.EncryptionResultRepository;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionResultService {

    private final PdfGeneratorService pdfGeneratorService;
    private final EncryptionResultRepository encryptionResultRepository;
    private final UserService userService;

    public void saveEncryptionResult(BlockCipherKey blockCipherKey, String inputText, String resultText) {
        log.info("Saving results for: {}, {}", inputText, resultText);
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        //todo signature for pdfs
        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                resultTextPdf,
                null, // todo Impl signature logic
                blockCipherKey.getUser(),
                null,
                blockCipherKey
        );
        encryptionResultRepository.save(encryptionResult);
    }

    public void saveRsaEncryptionResult(RSAKey rsaKey, String inputText, String resultText) {
        log.info("Saving results for: {}, {}", inputText, resultText);
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        //todo signature for pdfs
        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                resultTextPdf,
                null, // todo Impl signature logic
                rsaKey.getUser(),
                rsaKey,
                null
        );
        encryptionResultRepository.save(encryptionResult);
    }

    public List<EncryptionResult> findAllResults(String authorizationHeader) {
        User user = userService.findByAuthorizationHeader(authorizationHeader);
        return encryptionResultRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }
}
