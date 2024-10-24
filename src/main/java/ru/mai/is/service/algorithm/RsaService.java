package ru.mai.is.service.algorithm;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.mai.is.algorithm.rsa.RsaCipherImpl;

import lombok.extern.slf4j.Slf4j;

import static ru.mai.is.algorithm.rsa.RsaCipherImpl.saveKeyPairToFile;

@Service
@Slf4j
public class RsaService {
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

    public String encrypt(String text) {
        if (keyPair == null) {
            throw new IllegalStateException("RSA keys are not initialized yet. Please try again later.");
        }
        byte[] messageBytes = text.getBytes();
        byte[] encryptedBytes = RsaCipherImpl.encrypt(messageBytes, keyPair.getPublicKey());
        return new BigInteger(1, encryptedBytes).toString(16);
    }

    public String decrypt(String encryptedText) {
        if (keyPair == null) {
            throw new IllegalStateException("RSA keys are not initialized yet. Please try again later.");
        }
        byte[] encryptedBytes = new BigInteger(encryptedText, 16).toByteArray();
        byte[] decryptedBytes = RsaCipherImpl.decrypt(encryptedBytes, keyPair.getPrivateKey());
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
