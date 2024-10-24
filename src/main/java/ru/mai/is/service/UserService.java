package ru.mai.is.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mai.is.dto.request.RegistrationRequest;
import ru.mai.is.dto.request.StribogRequest;
import ru.mai.is.model.Role;
import ru.mai.is.model.User;
import ru.mai.is.repository.RoleRepository;
import ru.mai.is.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StribogService stribogService;

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Метод регистрации нового пользователя в системе
     * @param request Запрос на регистрацию пользователя
     */
    @Transactional
    public User registrationUser(RegistrationRequest request) {
        log.info("Start registration user: {}", request.getUsername());
        Role defaultRole = roleRepository.findByRole(Role.RoleEnum.DEFAULT_USER)
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        User user = new User(
                request.getUsername(),
                hashAndSaltPassword(request.getUsername(), request.getPassword()),
                request.getEmail(),
                request.getPhone(),
                Set.of(defaultRole)
        );
        return saveUser(user);
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
        // Generate random bytes
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16]; // 128 bits
        random.nextBytes(randomBytes);
        String randomPart = Hex.toHexString(randomBytes);

        // Use user-specific data
        String userData = username + passwordHash;

        // Combine and hash them to create the final salt
        String combined = randomPart + userData;

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
}
