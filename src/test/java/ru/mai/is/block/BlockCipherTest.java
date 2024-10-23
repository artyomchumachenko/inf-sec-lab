package ru.mai.is.block;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import ru.mai.is.algorithm.stribog.util.StringToByteArray;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BlockCipherTest {

    @Test
    void kuznechikEncryptionDecryptionTest() throws Exception {
        // 1. Добавляем провайдер Bouncy Castle
        Security.addProvider(new BouncyCastleProvider());

        // 2. Генерируем ключ для алгоритма Кузнечик (256 бит = 32 байта)
        byte[] keyBytes = new byte[32];
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) i; // Для теста используем фиксированные значения
        }
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "GOST3412-2015");

        // 3. Генерируем инициализационный вектор (IV) для режима CBC (128 бит = 16 байт)
        byte[] ivBytes = new byte[16];
        for (int i = 0; i < ivBytes.length; i++) {
            ivBytes[i] = (byte) (i + 1); // Фиксированные значения для теста
        }
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // 4. Подготавливаем данные для шифрования
        String plaintextString = "1122334455667700ffeeddccbbaa9988";
        byte[] plaintext = plaintextString.getBytes(StandardCharsets.UTF_8);

        // 5. Инициализируем шифр для шифрования с режимом CBC и дополнением PKCS#5
        Cipher cipher = Cipher.getInstance("GOST3412-2015/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // 6. Шифруем данные
        byte[] encryptedData = cipher.doFinal(plaintext);
        String encryptedString = StringToByteArray.bytesToHex(encryptedData);
        // 7. Инициализируем шифр для расшифровки
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 8. Расшифровываем данные
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // 9. Проверяем, что исходные и расшифрованные данные совпадают
        assertArrayEquals(plaintext, decryptedData);
    }

    @Test
    void magmaEncryptionDecryptionTest() throws Exception {
        // Добавляем провайдер Bouncy Castle
        Security.addProvider(new BouncyCastleProvider());

        // Генерируем ключ для Магмы (256 бит = 32 байта)
        byte[] keyBytes = new byte[32]; // 256-битный ключ
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) (i + 2);
        }
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "GOST28147");

        // Генерируем инициализационный вектор (IV) для режима CBC (64 бита = 8 байт)
        byte[] ivBytes = new byte[8]; // 64-битный IV
        for (int i = 0; i < ivBytes.length; i++) {
            ivBytes[i] = (byte) (i + 3);
        }
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // Подготавливаем данные для шифрования
        String plaintextString = "Привет, Магма!";
        byte[] plaintext = plaintextString.getBytes(StandardCharsets.UTF_8);

        // Инициализируем шифр для шифрования с режимом CBC и дополнением PKCS#5
        Cipher cipher = Cipher.getInstance("GOST28147/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // Шифруем данные
        byte[] encryptedData = cipher.doFinal(plaintext);

        // Инициализируем шифр для расшифровки
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // Расшифровываем данные
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Проверяем, что исходные и расшифрованные данные совпадают
        assertArrayEquals(plaintext, decryptedData);
    }
}
