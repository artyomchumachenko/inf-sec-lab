package ru.mai.is.service;

import java.io.ByteArrayOutputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfGeneratorService {

    public byte[] generatePdfByText(String text) {
        log.info("Generate pdf for text: {}", text);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Создаем PdfWriter с использованием ByteArrayOutputStream
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Добавляем текст в документ
            document.add(new Paragraph(text));

            // Закрываем документ
            document.close();

            // Возвращаем PDF в виде массива байт
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
