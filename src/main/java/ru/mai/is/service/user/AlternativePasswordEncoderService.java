package ru.mai.is.service.user;

import java.security.SecureRandom;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ru.mai.is.dto.request.algorithm.StribogRequest;
import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.service.algorithm.StribogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "password.encoder.type", havingValue = "v2")
public class AlternativePasswordEncoderService implements PasswordEncoder {

    private final StribogService stribogService;

    @Override
    public String encode(String username, String password) {
        return hashAndSaltPassword(username, password);
    }

    private String hashAndSaltPassword(String username, String password) {
        log.info("Start hashing password for user: {}", username);

        // Шаг 1: Генерация случайной соли
        String salt = generateRandomSalt();
        log.info("Generated salt for user {}: {}", username, salt);

        // Шаг 2: Продвинутое комбинирование пароля и соли
        String combinedHash = advancedCombinePasswordAndSalt(password, salt);

        // Шаг 3: Хэширование комбинированной строки
        StribogRequest finalHashRequest = StribogRequest.builder()
                .text(combinedHash)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String finalHash = stribogService.getHash(finalHashRequest);
        log.info("Password hash for user {}: {}", username, finalHash);

        // Шаг 4: Встраивание соли в сохранённый хэш
        String saltedHash = salt + ":" + finalHash;

        return saltedHash;
    }

    private String generateRandomSalt() {
        // Генерация случайной соли длиной 16 байт
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Hex.toHexString(saltBytes);
    }

    private String advancedCombinePasswordAndSalt(String password, String salt) {
        // Хэшируем пароль
        StribogRequest passwordHashRequest = StribogRequest.builder()
                .text(password)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String passwordHash = stribogService.getHash(passwordHashRequest);

        // Хэшируем соль
        StribogRequest saltHashRequest = StribogRequest.builder()
                .text(salt)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String saltHash = stribogService.getHash(saltHashRequest);

        // Комбинируем хэши пароля и соли с помощью побитового XOR
        String combinedHash = xorHexStrings(passwordHash, saltHash);

        return combinedHash;
    }

    private String xorHexStrings(String hex1, String hex2) {
        byte[] bytes1 = Hex.decode(hex1);
        byte[] bytes2 = Hex.decode(hex2);

        int minLength = Math.min(bytes1.length, bytes2.length);
        byte[] result = new byte[minLength];

        for (int i = 0; i < minLength; i++) {
            result[i] = (byte) (bytes1[i] ^ bytes2[i]);
        }

        return Hex.toHexString(result);
    }

    @Override
    public boolean matches(LoginRequest loginRequest, String expectedPassword) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Разделяем сохранённый хэш, чтобы получить соль и хэш
        String[] parts = expectedPassword.split(":", 2);
        if (parts.length != 2) {
            log.error("Invalid stored password format for user: {}", username);
            return false;
        }
        String salt = parts[0];
        String storedHash = parts[1];

        // Повторяем Шаги 2 и 3 для проверки пароля
        String combinedHash = advancedCombinePasswordAndSalt(password, salt);

        StribogRequest finalHashRequest = StribogRequest.builder()
                .text(combinedHash)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String computedHash = stribogService.getHash(finalHashRequest);

        // Сравниваем вычисленный хэш с сохранённым
        boolean matches = computedHash.equals(storedHash);
        log.info("Password match result for user {}: {}", username, matches);

        return matches;
    }
}
