package com.example.courseplatform.config;

import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Changed from BCryptPasswordEncoder

    @Override
    public void run(String... args) throws Exception {
        // Check if we already have users
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("admin");
            userRepository.save(admin);

            // Create student user
            User student = new User();
            student.setName("Student User");
            student.setEmail("student@example.com");
            student.setPassword(passwordEncoder.encode("student"));
            student.setRole("student");
            userRepository.save(student);

            System.out.println("Sample users created");
        }

        // Check if we already have courses
        if (courseRepository.count() == 0) {
            // Create some sample courses
            User admin = userRepository.findByEmail("admin@example.com");

            Course javaCourse = new Course();
            javaCourse.setTitle("Java Programming Fundamentals");
            javaCourse.setDescription("Learn the basics of Java programming language with hands-on examples.");
            javaCourse.setDuration("8 weeks");
            javaCourse.setCreatedBy(admin);
            courseRepository.save(javaCourse);

            Course springCourse = new Course();
            springCourse.setTitle("Spring Boot Development");
            springCourse.setDescription("Build web applications using Spring Boot, Spring MVC, and Spring Data JPA.");
            springCourse.setDuration("10 weeks");
            springCourse.setCreatedBy(admin);
            courseRepository.save(springCourse);

            Course webCourse = new Course();
            webCourse.setTitle("Web Development with HTML & CSS");
            webCourse.setDescription("Create responsive websites using modern HTML5 and CSS3 techniques.");
            webCourse.setDuration("6 weeks");
            webCourse.setCreatedBy(admin);
            courseRepository.save(webCourse);

            System.out.println("Sample courses created");
        }
    }
}
