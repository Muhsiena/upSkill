package com.example.courseplatform.repository;

import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Enrollment;
import com.example.courseplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);
    List<Enrollment> findByCourse(Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    List<Enrollment> findByUserAndCompletionStatus(User user, Enrollment.CompletionStatus status);
}
