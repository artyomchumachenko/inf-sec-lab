package ru.mai.is.service;

import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import static ru.mai.is.service.OtpService.OTP_VALID_DURATION;

@Service
@Slf4j
public class QrCodeService {

    private final Map<String, String> qrCodeStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> qrCodeTimestamp = new ConcurrentHashMap<>();
    private final Random random = new SecureRandom();

    // Генерирует 6-значный код и связывает его с пользователем
    public String generateRandomCodeForUser(String username) {
        String code = String.format("%06d", random.nextInt(999999));

        qrCodeStorage.put(username, code);
        qrCodeTimestamp.put(username, System.currentTimeMillis());
        return code;
    }

    // Генерирует изображение QR-кода на основе переданного текста
    public BufferedImage generateQrCodeImage(String text) throws WriterException {
        int width = 250;
        int height = 250;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    // Проверяет, соответствует ли введенный код сгенерированному и не истек ли срок действия
    public boolean verifyCode(String username, String code) {
        String storedCode = qrCodeStorage.get(username);
        Long timestamp = qrCodeTimestamp.get(username);

        if (storedCode == null || timestamp == null) {
            return false;
        }

        // Проверяем, не истек ли срок действия кода
        if (System.currentTimeMillis() - timestamp > OTP_VALID_DURATION) {
            // Удаляем просроченный код
            qrCodeStorage.remove(username);
            qrCodeTimestamp.remove(username);
            return false;
        }

        // Проверяем соответствие кодов
        if (storedCode.equals(code)) {
            // Удаляем использованный код
            qrCodeStorage.remove(username);
            qrCodeTimestamp.remove(username);
            return true;
        } else {
            return false;
        }
    }
}
