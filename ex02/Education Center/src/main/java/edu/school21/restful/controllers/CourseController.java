package edu.school21.restful.controllers;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.CourseState;
import edu.school21.restful.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/courses/{id}")
@RequiredArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    @PostMapping("/publish")
    public ResponseEntity<Object> publish(@PathVariable("id") Course course) {
        if (course == null || course.getState() == CourseState.PUBLISHED) {
            return ResponseEntity.notFound().build();
        }
        course.setState(CourseState.PUBLISHED);
        courseRepository.save(course);
        return ResponseEntity.ok(null);
    }
}
