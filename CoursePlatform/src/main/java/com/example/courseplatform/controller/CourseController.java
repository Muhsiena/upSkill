package com.example.courseplatform.controller;

import com.example.courseplatform.model.Enrollment;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.EnrollmentRepository;
import com.example.courseplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository; // Added UserRepository

    @GetMapping("/courses")
    public String listCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    @GetMapping("/courses/{id}")
    public String viewCourse(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails principal,
                             Model model) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course Id"));
        
        // Fetch user from repository using authenticated email
        User user = userRepository.findByEmail(principal.getUsername());
        Optional<Enrollment> enrollment = enrollmentRepository.findByUserAndCourse(user, course);
        
        model.addAttribute("course", course);
        enrollment.ifPresent(e -> model.addAttribute("enrollment", e));
        
        return "courses/details";
    }

    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId,
                         @AuthenticationPrincipal UserDetails principal) { // Changed to UserDetails
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        // Get user from repository
        User user = userRepository.findByEmail(principal.getUsername());

        try {
            if (enrollmentRepository.findByUserAndCourse(user, course).isPresent()) {
                return "redirect:/courses/" + courseId + "?error=already_enrolled";
            }
            
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollmentRepository.save(enrollment);
            
        } catch (IncorrectResultSizeDataAccessException | DataIntegrityViolationException e) {
            return "redirect:/courses/" + courseId + "?error=already_enrolled";
        }

        return "redirect:/courses/" + courseId;
    }
}
