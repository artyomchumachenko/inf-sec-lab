package ru.mai.is.service.user;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mai.is.dto.request.algorithm.StribogRequest;
import ru.mai.is.dto.request.hash.EncodeRequest;
import ru.mai.is.dto.request.hash.HashRequest;
import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.dto.request.user.RegistrationRequest;
import ru.mai.is.model.Role;
import ru.mai.is.model.User;
import ru.mai.is.repository.RoleRepository;
import ru.mai.is.repository.UserRepository;
import ru.mai.is.service.algorithm.StribogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StribogService stribogService;

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод регистрации нового пользователя в системе
     * @param request Запрос на регистрацию пользователя
     */
    @Transactional
    public User registration(RegistrationRequest request) {
        log.info("Start registration user: {}", request.getUsername());
        Role defaultRole = roleRepository.findByRole(Role.RoleEnum.DEFAULT_USER)
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getUsername(), request.getPassword()),
                request.getEmail(),
                request.getPhone(),
                Set.of(defaultRole)
        );
        return saveUser(user);
    }

    @Transactional(readOnly = true)
    public User findByAuthorizationHeader(String authorizationHeader) {
        String username = jwtTokenProvider.getUsernameFromAuthorizationHeader(authorizationHeader);
        return findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User verifyCredentials(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (!passwordEncoder.matches(loginRequest, user.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        return user;
    }


    public String getStribogHash(HashRequest hashRequest) {
        StribogRequest stribogRequest = StribogRequest.builder()
                .text(hashRequest.getInput())
                .mode(StribogRequest.StribogMode.MODE_512)
                .build();
        return stribogService.getHash(stribogRequest);
    }

    public String encodePassword(EncodeRequest encodeRequest) {
        return passwordEncoder.encode(encodeRequest.getUsername(), encodeRequest.getPassword());
    }

    public String generateToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
    }
}
