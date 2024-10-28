package ru.mai.is.service.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.dto.request.user.RegistrationRequest;
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
    private final PasswordEncoderService passwordEncoderService;
    private final JwtTokenProvider jwtTokenProvider;

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
    public User registration(RegistrationRequest request) {
        log.info("Start registration user: {}", request.getUsername());
        Role defaultRole = roleRepository.findByRole(Role.RoleEnum.DEFAULT_USER)
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        User user = new User(
                request.getUsername(),
                passwordEncoderService.encode(request.getUsername(), request.getPassword()),
                request.getEmail(),
                request.getPhone(),
                Set.of(defaultRole)
        );
        return saveUser(user);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (passwordEncoderService.matches(loginRequest, user.getPassword())) {
            // Генерируем JWT токен при успешной аутентификации
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new IllegalArgumentException("Неверный пароль");
        }
    }
}
