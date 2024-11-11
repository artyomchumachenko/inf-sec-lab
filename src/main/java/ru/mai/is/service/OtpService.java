package ru.mai.is.service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import ru.mai.is.model.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OtpService {

    /** Время жизни OTP в миллисекундах (например, 5 минут = 5 * 60 * 1000) */
    public static final long OTP_VALID_DURATION = 5 * 60 * 1000;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new SecureRandom();

    private final Map<String, Long> otpTimestamp = new ConcurrentHashMap<>();

    private final EmailService emailService;

    public OtpService(EmailService emailService) {
        this.emailService = emailService;
    }

    /** Метод для генерации и отправки OTP */
    public void generateAndSendOTP(User user) {
        String username = user.getUsername();

        // Генерируем 6-значный OTP
        String otp = String.format("%06d", random.nextInt(999999));

        // Сохраняем OTP и временную метку
        otpStorage.put(username, otp);
        otpTimestamp.put(username, System.currentTimeMillis());

        // Отправка OTP пользователю (например, по email)
        // Для упрощения будем выводить в консоль
        log.info("OTP для пользователя {}: {}", username, otp);
        String subject = "Ваш OTP код";
        String body = "Ваш OTP код для входа: " + otp + "\nОн действителен в течение 5 минут.";
        emailService.sendOtpEmail(user.getEmail(), subject, body);
    }

    /** Метод для проверки OTP */
    public boolean verifyOtp(String username, String otp) {
        String storedOtp = otpStorage.get(username);
        Long timestamp = otpTimestamp.get(username);

        // Проверяем, существует ли OTP для данного пользователя
        if (storedOtp == null || timestamp == null) {
            return false;
        }

        // Проверяем, не истек ли срок действия OTP
        if (System.currentTimeMillis() - timestamp > OTP_VALID_DURATION) {
            // Удаляем просроченный OTP
            otpStorage.remove(username);
            otpTimestamp.remove(username);
            return false;
        }

        // Проверяем соответствие OTP
        if (storedOtp.equals(otp)) {
            // Удаляем использованный OTP
            otpStorage.remove(username);
            otpTimestamp.remove(username);
            return true;
        } else {
            return false;
        }
    }
}
