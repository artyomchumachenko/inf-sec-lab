package ru.mai.is.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PdfSignerService {

    static {
        // Добавляем провайдер BouncyCastle для поддержки нужных алгоритмов
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] generateSignature(byte[] data, PrivateKey privateKey) {
        try {
            // Создаем объект для подписи с указанием алгоритма, например, SHA256withRSA
            Signature signature = Signature.getInstance("SHA256withRSA");

            // Инициализируем подпись закрытым ключом
            signature.initSign(privateKey);

            // Добавляем данные, которые нужно подписать
            signature.update(data);

            // Возвращаем подпись в виде массива байтов
            return signature.sign();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // Метод для добавления подписи в PDF
    public static byte[] signPdfWithGeneratedSignature(byte[] pdfData, byte[] signatureBytes) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(pdfData));
        int lastPage = pdfReader.getNumberOfPages(); // Получаем номер последней страницы
        PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, outputStream, '\0');

        PdfSignatureAppearance appearance = pdfStamper.getSignatureAppearance();
        appearance.setReason("Document signed");
        appearance.setLocation("Encryption Tool");

        // Установка позиции и размера области подписи на последней странице
        Rectangle signatureRectangle = new Rectangle(100, 100, 400, 250); // Координаты области подписи
        appearance.setVisibleSignature(signatureRectangle, lastPage, "sig"); // Устанавливаем подпись на последней странице

        // Используем внешнюю подпись в формате PKCS#7
        ExternalSignatureContainer external = new ExternalSignatureContainer() {
            public byte[] sign(InputStream is) {
                return signatureBytes; // Возвращаем ранее сгенерированную подпись
            }

            @Override
            public void modifySigningDictionary(PdfDictionary pdfDictionary) {

            }
        };

        MakeSignature.signExternalContainer(appearance, external, 8192);
        pdfStamper.close();
        pdfReader.close();

        return outputStream.toByteArray();
    }
}
