package ru.mai.is.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.mai.is.dto.request.hash.EncodeRequest;
import ru.mai.is.dto.request.hash.HashRequest;
import ru.mai.is.dto.request.mfa.OtpRequest;
import ru.mai.is.dto.request.mfa.QrCodeRequest;
import ru.mai.is.dto.request.user.LoginRequest;
import ru.mai.is.dto.request.user.RegistrationRequest;
import ru.mai.is.model.User;
import ru.mai.is.service.OtpService;
import ru.mai.is.service.QrCodeService;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OtpService otpService;
    private final QrCodeService qrCodeService;

    @PostMapping("/registration")
    public ResponseEntity<User> registrationUser(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(userService.registration(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Проверяем логин и пароль
            User user = userService.verifyCredentials(loginRequest);

            // Генерируем и отправляем OTP
            otpService.generateAndSendOTP(user);
            return ResponseEntity.ok("OTP отправлен на ваш email."); // todo Front
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        if (otpService.verifyOtp(otpRequest.getUsername(), otpRequest.getOtp())) {
            // Переходим к следующему фактору
            return ResponseEntity.ok("OTP подтвержден. Пожалуйста, сканируйте QR-код."); // todo Front
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный OTP");
        }
    }

    @GetMapping("/generate-qr")
    public void generateQrCode(HttpServletResponse response, @RequestParam String username) throws Exception {
        String randomCode = qrCodeService.generateRandomCodeForUser(username);
        BufferedImage qrImage = qrCodeService.generateQrCodeImage(randomCode);

        response.setContentType("image/png");
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(qrImage, "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/verify-qr-code")
    public ResponseEntity<?> verifyQrCode(@RequestBody QrCodeRequest qrCodeRequest) {
        if (qrCodeService.verifyCode(qrCodeRequest.getUsername(), qrCodeRequest.getCode())) {
            // Аутентификация успешна, генерируем токен
            String token = userService.generateToken(qrCodeRequest.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный код из QR-кода");
        }
    }

    @PostMapping("/hash")
    public ResponseEntity<String> getStribogHash(@RequestBody HashRequest hashRequest) {
        String hash = userService.getStribogHash(hashRequest);
        return ResponseEntity.ok(hash);
    }

    @PostMapping("/encode")
    public ResponseEntity<String> encodePassword(@RequestBody EncodeRequest encodeRequest) {
        String encodedPassword = userService.encodePassword(encodeRequest);
        return ResponseEntity.ok(encodedPassword);
    }
}
