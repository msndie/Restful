package edu.school21.restful.controllers;

import edu.school21.restful.model.BadRequest;
import edu.school21.restful.model.Course;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;

    @Autowired
    public CoursesController(CourseService courseService, LessonService lessonService, UserService userService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody Course course) {
        System.out.println(course);
        if (course.getDescription() == null || course.getName() == null
            || course.getStartDate() == null || course.getEndDate() == null
            || course.getStartDate().isAfter(course.getEndDate())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", BadRequest.getInstance()));
        }
        courseService.save(course);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(Collections.singletonMap("error", BadRequest.getInstance())));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> putId(@PathVariable Long id, @RequestBody Course course) {
        if (course.getDescription() == null || course.getName() == null
                || course.getStartDate() == null || course.getEndDate() == null
                || course.getStartDate().isAfter(course.getEndDate())
                || !courseService.existsById(id)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", BadRequest.getInstance()));
        }
        course.setId(id);
        courseService.save(course);
        return ResponseEntity.ok(course);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteId(@PathVariable Long id) {
        if (courseService.existsById(id)) {
            courseService.deleteById(id);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", BadRequest.getInstance()));
    }

    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdLessons(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value.getLessons()))
                .orElseGet(() -> ResponseEntity.badRequest().body(Collections.singletonMap("error", BadRequest.getInstance())));
    }
}
