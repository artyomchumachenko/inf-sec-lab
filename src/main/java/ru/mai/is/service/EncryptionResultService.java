package ru.mai.is.service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
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

import static ru.mai.is.service.PdfSignerService.generateSignature;
import static ru.mai.is.service.PdfSignerService.signPdfWithGeneratedSignature;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncryptionResultService {

    private final PdfGeneratorService pdfGeneratorService;
    private final EncryptionResultRepository encryptionResultRepository;
    private final UserService userService;

    public void saveEncryptionResult(BlockCipherKey blockCipherKey, String inputText, String resultText) {
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);

        String keystorePath = "src/main/resources/mykeystore.p12";
        String keystorePassword = "keystorePassword";
        String alias = "myalias";
        byte[] signatureBytes;
        byte[] signedResultPdf;
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keystore.load(fis, keystorePassword.toCharArray());
            }

            PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePassword.toCharArray());
            signatureBytes = generateSignature(resultTextPdf, privateKey);
            signedResultPdf = signPdfWithGeneratedSignature(resultTextPdf, signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                signedResultPdf,
                signatureBytes,
                blockCipherKey.getUser(),
                null,
                blockCipherKey
        );
        encryptionResultRepository.save(encryptionResult);
    }

    public void saveRsaEncryptionResult(RSAKey rsaKey, String inputText, String resultText) {
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);

        String keystorePath = "src/main/resources/mykeystore.p12";
        String keystorePassword = "keystorePassword";
        String alias = "myalias";
        byte[] signatureBytes;
        byte[] signedResultPdf;
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keystore.load(fis, keystorePassword.toCharArray());
            }

            PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePassword.toCharArray());
            signatureBytes = generateSignature(resultTextPdf, privateKey);
            signedResultPdf = signPdfWithGeneratedSignature(resultTextPdf, signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EncryptionResult encryptionResult = new EncryptionResult(
                inputTextPdf,
                signedResultPdf,
                signatureBytes,
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
