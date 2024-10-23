package ru.mai.is.algorithm.rsa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

import lombok.Getter;

public class RsaCipherImpl {
    private static final int BIT_LENGTH = 16384; // Длина ключа в битах
    private static final SecureRandom secureRandom = new SecureRandom();

    // Генерация пары ключей RSA
    public static KeyPair generateKeyPair() {
        BigInteger p = BigInteger.probablePrime(RsaCipherImpl.BIT_LENGTH / 2, secureRandom);
        BigInteger q = BigInteger.probablePrime(RsaCipherImpl.BIT_LENGTH / 2, secureRandom);
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537); // Часто используемое значение для e

        // Вычисление d, модульного обратного элемента e
        BigInteger d = e.modInverse(phi);

        RSAKey publicKey = new RSAKey(e, n);
        RSAKey privateKey = new RSAKey(d, n);
        return new KeyPair(publicKey, privateKey);
    }

    // Шифрование сообщения в виде массива байтов
    public static byte[] encrypt(byte[] message, RSAKey publicKey) {
        BigInteger messageBigInt = new BigInteger(1, message); // Преобразуем байты в BigInteger
        BigInteger encryptedBigInt = messageBigInt.modPow(publicKey.exponent, publicKey.modulus); // Шифрование BigInteger
        return encryptedBigInt.toByteArray(); // Преобразуем результат обратно в массив байтов
    }

    // Расшифровка сообщения в виде массива байтов
    public static byte[] decrypt(byte[] encrypted, RSAKey privateKey) {
        BigInteger encryptedBigInt = new BigInteger(1, encrypted); // Преобразуем байты в BigInteger
        BigInteger decryptedBigInt = encryptedBigInt.modPow(privateKey.exponent, privateKey.modulus); // Расшифровка BigInteger
        return decryptedBigInt.toByteArray(); // Преобразуем результат обратно в массив байтов
    }

    // Обновленный класс RSAKey должен реализовывать Serializable
    public static class RSAKey implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        BigInteger exponent;
        BigInteger modulus;

        RSAKey(BigInteger exponent, BigInteger modulus) {
            this.exponent = exponent;
            this.modulus = modulus;
        }
    }

    // Обновленный класс KeyPair
    @Getter
    public static class KeyPair implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        RSAKey publicKey;
        RSAKey privateKey;

        KeyPair(RSAKey publicKey, RSAKey privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    // Сохранение пары ключей в файлы
    public static void saveKeyPairToFile(KeyPair keyPair, String publicKeyPath, String privateKeyPath) throws
            IOException {
        // Сохранение открытого ключа
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(publicKeyPath))) {
            oos.writeObject(keyPair.getPublicKey());
        }
        // Сохранение закрытого ключа
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(privateKeyPath))) {
            oos.writeObject(keyPair.getPrivateKey());
        }
    }

    // Загрузка пары ключей из файлов
    public static KeyPair loadKeyPairFromFile(String publicKeyPath, String privateKeyPath) throws IOException, ClassNotFoundException {
        // Загрузка открытого ключа
        RSAKey publicKey;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(publicKeyPath))) {
            publicKey = (RSAKey) ois.readObject();
        }
        // Загрузка закрытого ключа
        RSAKey privateKey;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(privateKeyPath))) {
            privateKey = (RSAKey) ois.readObject();
        }
        return new KeyPair(publicKey, privateKey);
    }
}
