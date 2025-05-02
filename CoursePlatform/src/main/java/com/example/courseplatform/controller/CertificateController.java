package com.example.courseplatform.controller;

import com.example.courseplatform.model.Enrollment;
import com.example.courseplatform.repository.EnrollmentRepository;
import com.example.courseplatform.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;

@Controller
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping("/complete/{enrollmentId}")
    public ResponseEntity<byte[]> markCompleteAndDownload(
            @PathVariable Long enrollmentId,
            @AuthenticationPrincipal UserDetails authenticatedUser) throws IOException {
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment ID"));

        if (!enrollment.getUser().getEmail().equals(authenticatedUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        enrollment.setCompletionStatus(Enrollment.CompletionStatus.Completed);
        enrollmentRepository.save(enrollment);

        byte[] pdfBytes = certificateService.generateCertificate(
                enrollment.getUser(),
                enrollment.getCourse()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "certificate-" + enrollmentId + ".pdf"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/certificates/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable("id") Long enrollmentId,
            @AuthenticationPrincipal UserDetails authenticatedUser) throws IOException {
        
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid enrollment ID"));

        if (!enrollment.getUser().getEmail().equals(authenticatedUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        byte[] pdfBytes = certificateService.generateCertificate(
                enrollment.getUser(),
                enrollment.getCourse()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "certificate-" + enrollmentId + ".pdf"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
