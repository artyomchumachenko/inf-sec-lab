package ru.mai.is.service;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import ru.mai.is.dto.request.BlockRequest;

import lombok.extern.slf4j.Slf4j;

import static ru.mai.is.algorithm.stribog.util.StringToByteArray.bytesToHex;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.castStringToBytes;

@Service
@Slf4j
public class BlockService {

    private final byte[] keyBytes;

    public BlockService() {
        Security.addProvider(new BouncyCastleProvider());
        this.keyBytes = castStringToBytes(
                "8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef"
        );
    }

    public String encrypt(String text, BlockRequest.BlockMode mode) {
        if (mode.equals(BlockRequest.BlockMode.MODE_128)) {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "GOST3412-2015");

            // Открытый текст (128 бит = 16 байт)
            byte[] plaintext = castStringToBytes(text);

            // Инициализируем шифр для шифрования
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("GOST3412-2015/ECB/NoPadding", "BC");

                cipher.init(Cipher.ENCRYPT_MODE, keySpec);

                // Шифруем данные
                byte[] encryptedData = cipher.doFinal(plaintext);

                return bytesToHex(encryptedData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (mode.equals(BlockRequest.BlockMode.MODE_64)) {
            return "Unsupported encryption format";
        }

        return "Unsupported encryption format";
    }

    public String decrypt(String encryptedText, BlockRequest.BlockMode mode) {
        if (mode.equals(BlockRequest.BlockMode.MODE_128)) {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "GOST3412-2015");

            // Зашифрованный текст в виде массива байт (128 бит = 16 байт)
            byte[] encryptedData = castStringToBytes(encryptedText);

            // Инициализируем шифр для расшифрования
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("GOST3412-2015/ECB/NoPadding", "BC");

                cipher.init(Cipher.DECRYPT_MODE, keySpec);

                // Расшифровываем данные
                byte[] decryptedData = cipher.doFinal(encryptedData);

                return bytesToHex(decryptedData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (mode.equals(BlockRequest.BlockMode.MODE_64)) {
            return "Unsupported decryption format";
        }

        return "Unsupported decryption format";
    }
}
