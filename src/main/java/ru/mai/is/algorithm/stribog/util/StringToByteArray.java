package ru.mai.is.algorithm.stribog.util;

public class StringToByteArray {

    /**
     * Преобразует шестнадцатеричную строку в массив байтов.
     *
     * @param hex шестнадцатеричная строка
     * @return массив байтов
     */
    public static byte[] castStringToBytes(String hex) {
        // Если длина строки нечетная, добавляем ведущий ноль
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Преобразует массив байтов в строку шестнадцатеричных значений для удобного отображения.
     *
     * @param bytes массив байтов для преобразования
     * @return строка в шестнадцатеричном формате
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

