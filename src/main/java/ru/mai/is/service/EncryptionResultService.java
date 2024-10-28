package ru.mai.is.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import ru.mai.is.config.properties.KeystoreProperties;
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
    private final KeystoreProperties keystoreProperties;
    private final ResourceLoader resourceLoader;

    public void saveBlockCipherEncryptionResult(BlockCipherKey blockCipherKey, String inputText, String resultText) {
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        byte[] signedResultPdf = signResultPdf(resultTextPdf);

        EncryptionResult encryptionResult = buildEncryptionResult(inputTextPdf, signedResultPdf, blockCipherKey.getUser(), blockCipherKey, null);
        encryptionResultRepository.save(encryptionResult);
    }

    public void saveRsaEncryptionResult(RSAKey rsaKey, String inputText, String resultText) {
        byte[] inputTextPdf = pdfGeneratorService.generatePdfByText(inputText);
        byte[] resultTextPdf = pdfGeneratorService.generatePdfByText(resultText);
        byte[] signedResultPdf = signResultPdf(resultTextPdf);

        EncryptionResult encryptionResult = buildEncryptionResult(inputTextPdf, signedResultPdf, rsaKey.getUser(), null, rsaKey);
        encryptionResultRepository.save(encryptionResult);
    }

    public List<EncryptionResult> findAllResults(String authorizationHeader) {
        User user = userService.findByAuthorizationHeader(authorizationHeader);
        return encryptionResultRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }

    private byte[] signResultPdf(byte[] resultTextPdf) {
        byte[] signatureBytes;
        byte[] signedResultPdf;
        try {
            PrivateKey privateKey = loadPrivateKey();
            signatureBytes = generateSignature(resultTextPdf, privateKey);
            signedResultPdf = signPdfWithGeneratedSignature(resultTextPdf, signatureBytes);
        } catch (Exception e) {
            log.error("Error signing the PDF", e);
            throw new RuntimeException("Failed to sign PDF", e);
        }
        return signedResultPdf;
    }

    private PrivateKey loadPrivateKey() {
        try {
            Resource keystoreResource = resourceLoader.getResource(keystoreProperties.getPath());
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            try (InputStream is = keystoreResource.getInputStream()) {
                keystore.load(is, keystoreProperties.getPassword().toCharArray());
            }
            return (PrivateKey) keystore.getKey(keystoreProperties.getAlias(), keystoreProperties.getPassword().toCharArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private EncryptionResult buildEncryptionResult(byte[] inputTextPdf, byte[] signedResultPdf, User user, BlockCipherKey blockCipherKey, RSAKey rsaKey) {
        return new EncryptionResult(
                inputTextPdf,
                signedResultPdf,
                generateSignature(signedResultPdf, loadPrivateKey()),
                user,
                rsaKey,
                blockCipherKey
        );
    }
}
