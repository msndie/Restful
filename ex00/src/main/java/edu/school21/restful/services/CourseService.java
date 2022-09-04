package edu.school21.restful.services;

import edu.school21.restful.model.Course;

import java.util.Optional;

public interface CourseService extends Service<Course> {
    Optional<Course> getById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
