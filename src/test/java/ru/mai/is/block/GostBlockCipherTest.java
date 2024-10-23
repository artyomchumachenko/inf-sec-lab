package ru.mai.is.block;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.castStringToBytes;

public class GostBlockCipherTest {

    @Test
    void kuznechikGostTest() throws Exception {
        // Добавляем провайдер Bouncy Castle
        Security.addProvider(new BouncyCastleProvider());

        // Контрольный ключ (256 бит = 32 байта)
        byte[] keyBytes = castStringToBytes(
                "8899aabbccddeeff0011223344556677" +
                        "fedcba98765432100123456789abcdef"
        );

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "GOST3412-2015");

        // Открытый текст (128 бит = 16 байт)
        byte[] plaintext = castStringToBytes("1122334455667700ffeeddccbbaa9988");

        // Ожидаемый шифртекст
        byte[] expectedCiphertext = castStringToBytes("7f679d90bebc24305a468d42b9d4edcd");

        // Инициализируем шифр для шифрования
        Cipher cipher = Cipher.getInstance("GOST3412-2015/ECB/NoPadding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // Шифруем данные
        byte[] encryptedData = cipher.doFinal(plaintext);

        // Проверяем, что зашифрованные данные совпадают с ожидаемыми
        assertArrayEquals(expectedCiphertext, encryptedData);
    }
}
