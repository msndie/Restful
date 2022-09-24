package edu.school21.restful.controllers;

import edu.school21.restful.dto.CourseOut;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.CourseState;
import edu.school21.restful.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller @RequestMapping("/courses/{id}")
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    @PostMapping("/publish")
    public ResponseEntity<Object> publish(@PathVariable("id") Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent() || course.get().getState() == CourseState.PUBLISHED) {
            return ResponseEntity.notFound().build();
        }
        course.get().setState(CourseState.PUBLISHED);
        courseRepository.save(course.get());
        return ResponseEntity.ok(new CourseOut(course.get().getName(),
                course.get().getDescription(),
                course.get().getState(),
                course.get().getStartDate(),
                course.get().getEndDate()));
    }
}
