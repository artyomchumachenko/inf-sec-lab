package ru.mai.is.service.user;

import org.springframework.stereotype.Service;

import ru.mai.is.dto.request.algorithm.StribogRequest;
import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.service.algorithm.StribogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordEncoderService {

    private final StribogService stribogService;

    public String encode(String username, String password) {
        return hashAndSaltPassword(username, password);
    }

    private String hashAndSaltPassword(String username, String password) {
        log.info("Start hash password for user: {}", username);
        StribogRequest hashRequest = StribogRequest.builder()
                .text(password)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String passwordHash = stribogService.getHash(hashRequest);
        log.info("Finish hash: {} for user: {}", username, passwordHash);

        // Step 2: Generate a salt using a cool algorithm
        log.info("Start salt for user: {}", username);
        String salt = generateCoolSalt(username, passwordHash);
        log.info("Finish salt: {} for user: {}", salt, username);

        // Step 3: Combine the hashed password and the salt
        log.info("Start combine hash and salt for user: {}", username);
        String combineHashAndSalt = combineHashAndSalt(passwordHash, salt);
        log.info("Finish combine hash and salt password: {} for user: {}", combineHashAndSalt, username);
        return combineHashAndSalt;
    }

    private String generateCoolSalt(String username, String passwordHash) {
        // Use user-specific data
        StribogRequest usernameHashRequest = StribogRequest.builder()
                .text(username)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        String userDataHash = stribogService.getHash(usernameHashRequest) + passwordHash;

        // Combine and hash them to create the final salt
        String combined = passwordHash + userDataHash;

        StribogRequest saltHashRequest = StribogRequest.builder()
                .text(combined)
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();

        return stribogService.getHash(saltHashRequest);
    }

    private String combineHashAndSalt(String passwordHash, String salt) {
        // Interleave the characters of the password hash and the salt
        StringBuilder combined = new StringBuilder();
        int maxLength = Math.max(passwordHash.length(), salt.length());

        for (int i = 0; i < maxLength; i++) {
            if (i < passwordHash.length()) {
                combined.append(passwordHash.charAt(i));
            }
            if (i < salt.length()) {
                combined.append(salt.charAt(i));
            }
        }

        // Hash the combined string again
        StribogRequest finalHashRequest = StribogRequest.builder()
                .text(combined.toString())
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();

        return stribogService.getHash(finalHashRequest);
    }

    public boolean matches(LoginRequest loginRequest, String expectedPassword) {
        String actualEncodedPassword = encode(loginRequest.getUsername(), loginRequest.getPassword());
        return actualEncodedPassword.equals(expectedPassword);
    }
}
