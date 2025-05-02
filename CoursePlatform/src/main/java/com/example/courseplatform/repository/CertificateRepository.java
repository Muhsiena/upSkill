package com.example.courseplatform.repository;

import com.example.courseplatform.model.Certificate;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // Find all certificates for a specific user
    List<Certificate> findByUser(User user);
    
    // Find all certificates for a specific course
    List<Certificate> findByCourse(Course course);
    
    // Find certificate by user and course
    Certificate findByUserAndCourse(User user, Course course);
}
