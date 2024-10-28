package ru.mai.is.service.algorithm;

import java.security.Security;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import ru.mai.is.dto.request.algorithm.BlockRequest;
import ru.mai.is.dto.response.TextResponse;
import ru.mai.is.model.BlockCipherKey;
import ru.mai.is.model.User;
import ru.mai.is.service.EncryptionResultService;
import ru.mai.is.service.KeyService;
import ru.mai.is.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ru.mai.is.algorithm.stribog.util.StringToByteArray.bytesToHex;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.castStringToBytes;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockService {

    public static final String DEFAULT_BLOCK_CIPHER_KEY = "8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef";

    private final EncryptionResultService encryptionResultService;
    private final UserService userService;
    private final KeyService keyService;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public TextResponse encrypt(BlockRequest request, String authorizationHeader) {
        BlockRequest.BlockMode mode = request.getMode();
        String text = request.getText();
        String key = request.getKey() == null ? DEFAULT_BLOCK_CIPHER_KEY : request.getKey();
        User user = userService.findByAuthorizationHeader(authorizationHeader);

        if (mode.equals(BlockRequest.BlockMode.MODE_128)) {
            SecretKeySpec keySpec = new SecretKeySpec(castStringToBytes(key), "GOST3412-2015");

            // Открытый текст (128 бит = 16 байт)
            byte[] plaintext = castStringToBytes(text);

            // Инициализируем шифр для шифрования
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("GOST3412-2015/ECB/NoPadding", "BC");

                cipher.init(Cipher.ENCRYPT_MODE, keySpec);

                // Шифруем данные
                byte[] encryptedData = cipher.doFinal(plaintext);
                String encryptedText = bytesToHex(encryptedData);

                encryptionResultService.saveBlockCipherEncryptionResult(new BlockCipherKey(user, key), request.getText(), encryptedText);
                return new TextResponse(encryptedText);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (mode.equals(BlockRequest.BlockMode.MODE_64)) {
            return new TextResponse("Unsupported encryption format");
        }

        return new TextResponse("Unsupported encryption format");
    }

    public TextResponse decrypt(BlockRequest request, String authorizationHeader) {
        BlockRequest.BlockMode mode = request.getMode();
        String text = request.getText();
        String key = request.getKey() == null ? DEFAULT_BLOCK_CIPHER_KEY : request.getKey();
        User user = userService.findByAuthorizationHeader(authorizationHeader);

        if (mode.equals(BlockRequest.BlockMode.MODE_128)) {
            SecretKeySpec keySpec = new SecretKeySpec(castStringToBytes(key), "GOST3412-2015");

            // Зашифрованный текст в виде массива байт (128 бит = 16 байт)
            byte[] encryptedData = castStringToBytes(text);

            // Инициализируем шифр для расшифрования
            Cipher cipher;
            try {
                cipher = Cipher.getInstance("GOST3412-2015/ECB/NoPadding", "BC");

                cipher.init(Cipher.DECRYPT_MODE, keySpec);

                // Расшифровываем данные
                byte[] decryptedData = cipher.doFinal(encryptedData);
                String decryptedText = bytesToHex(decryptedData);

                encryptionResultService.saveBlockCipherEncryptionResult(new BlockCipherKey(user, key), request.getText(), decryptedText);
                return new TextResponse(decryptedText);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (mode.equals(BlockRequest.BlockMode.MODE_64)) {
            return new TextResponse("Unsupported decryption format");
        }

        return new TextResponse("Unsupported decryption format");
    }

    public TextResponse lastKey(String authorizationHeader) {
        User user = userService.findByAuthorizationHeader(authorizationHeader);
        return new TextResponse(keyService.getLastBlockCipherKeyByUser(user).getKeyData());
    }
}
