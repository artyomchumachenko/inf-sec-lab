package ru.mai.is.service.algorithm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.mai.is.algorithm.rsa.RsaCipherImpl;
import ru.mai.is.dto.request.algorithm.RsaRequest;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.model.RSAKey;
import ru.mai.is.model.User;
import ru.mai.is.service.EncryptionResultService;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ru.mai.is.algorithm.rsa.RsaCipherImpl.saveKeyPairToFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class RsaService {

    private final UserService userService;
    private final EncryptionResultService encryptionResultService;

    private volatile RsaCipherImpl.KeyPair keyPair;
    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";

    @PostConstruct
    public void init() {
        // Проверяем, существуют ли файлы ключей
        if (keysExist()) {
            // Если существуют, загружаем ключи
            try {
                keyPair = RsaCipherImpl.loadKeyPairFromFile(PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
                log.info("RSA keys loaded from files.");
            } catch (IOException | ClassNotFoundException e) {
                log.error("Failed to load RSA keys from files.", e);
            }
        } else {
            log.warn("The keys generation is necessary.");
        }
    }

    public void deleteKeyPair() {
        keyPair = null;
        log.info("Key pair removed from memory.");

        boolean publicKeyDeleted = deleteFile(PUBLIC_KEY_FILE);
        boolean privateKeyDeleted = deleteFile(PRIVATE_KEY_FILE);

        if (publicKeyDeleted && privateKeyDeleted) {
            log.info("RSA key files deleted successfully.");
        } else {
            log.warn("Failed to delete one or more RSA key files.");
        }
    }

    private boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        } else {
            log.warn("File {} does not exist and cannot be deleted.", fileName);
            return false;
        }
    }

    @Async
    public void generateKeysAsync() {
        if (keysExist()) {
            log.warn("Keys generated, firstly delete old keys.");
            return;
        }
        try {
            log.info("Starting asynchronous RSA key pair generation...");
            keyPair = RsaCipherImpl.generateKeyPair();
            saveKeyPairToFile(keyPair, PUBLIC_KEY_FILE, PRIVATE_KEY_FILE);
            log.info("RSA key pair generated and saved to files.");
        } catch (Exception e) {
            log.error("Error generating RSA key pair.", e);
        }
    }

    public boolean keysExist() {
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        return publicKeyFile.exists() && privateKeyFile.exists();
    }

    public TextResponse encrypt(RsaRequest request, String authorizationHeader) {
        if (keyPair == null) {
            throw new IllegalStateException("RSA keys are not initialized yet. Please try again later.");
        }

        String text = request.getText();
        User user = userService.findByAuthorizationHeader(authorizationHeader);

        byte[] messageBytes = request.getText().getBytes();
        byte[] encryptedBytes = RsaCipherImpl.encrypt(messageBytes, keyPair.getPublicKey());
        String encryptedText = new BigInteger(1, encryptedBytes).toString(16);

        encryptionResultService.saveRsaEncryptionResult(new RSAKey(user, serializeRSAKey(keyPair.getPublicKey()),
                        serializeRSAKey(keyPair.getPrivateKey())), text, encryptedText);
        return new TextResponse(encryptedText);
    }

    public TextResponse decrypt(RsaRequest request, String authorizationHeader) {
        if (keyPair == null) {
            throw new IllegalStateException("RSA keys are not initialized yet. Please try again later.");
        }

        String text = request.getText();
        User user = userService.findByAuthorizationHeader(authorizationHeader);

        byte[] encryptedBytes = new BigInteger(text, 16).toByteArray();
        byte[] decryptedBytes = RsaCipherImpl.decrypt(encryptedBytes, keyPair.getPrivateKey());
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8).substring(1);

        encryptionResultService.saveRsaEncryptionResult(new RSAKey(user, serializeRSAKey(keyPair.getPublicKey()),
                serializeRSAKey(keyPair.getPrivateKey())), text, decryptedText);
        return new TextResponse(decryptedText);
    }

    private byte[] serializeRSAKey(RsaCipherImpl.RSAKey key) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(key);
            oos.flush();
            byte[] keyBytes = baos.toByteArray();
            oos.close();
            return keyBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RsaCipherImpl.RSAKey deserializeRSAKey(byte[] keyBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(keyBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        RsaCipherImpl.RSAKey rsaKey = (RsaCipherImpl.RSAKey) ois.readObject();
        ois.close();
        return rsaKey;
    }
}
