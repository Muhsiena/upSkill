package com.example.courseplatform.repository;

import com.example.courseplatform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Find courses by title containing the search term
    List<Course> findByTitleContainingIgnoreCase(String title);
    
    // Find courses by description containing the search term
    List<Course> findByDescriptionContainingIgnoreCase(String description);
    
    // Find courses by both title and description
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Course> searchCourses(@Param("search") String search);
}
