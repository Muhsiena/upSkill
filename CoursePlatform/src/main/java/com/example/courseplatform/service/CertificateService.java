package com.example.courseplatform.service;

import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateService {
    
    public byte[] generateCertificate(User user, Course course) throws IOException {
        validateInputs(user, course);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        
        try (PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            
            addCertificateContent(document, user, course);
        }
        
        return baos.toByteArray();
    }

    private void validateInputs(User user, Course course) {
        if (user == null || course == null) {
            throw new IllegalArgumentException("User and Course must not be null");
        }
        if (user.getName() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("User name and Course title are required");
        }
    }

    private void addCertificateContent(Document document, User user, Course course) {
        document.add(new Paragraph("Certificate of Completion")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Awarded to:")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(user.getName())
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("For successfully completing:")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(course.getTitle())
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        String formattedDate = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

        document.add(new Paragraph("Completed on: " + formattedDate)
                .setTextAlignment(TextAlignment.CENTER));
    }
}
