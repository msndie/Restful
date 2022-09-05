package edu.school21.restful.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.school21.restful.model.BadRequest;
import edu.school21.restful.model.Course;
import edu.school21.restful.model.Lesson;
import edu.school21.restful.model.Role;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final Map<String, Object> error;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;

    @Autowired
    public CoursesController(CourseService courseService, LessonService lessonService, UserService userService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.error = Collections.singletonMap("error", BadRequest.getInstance());
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
            return ResponseEntity.badRequest().body(error);
        }
        courseService.save(course);
        return ResponseEntity.ok(course);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> putId(@PathVariable Long id, @RequestBody Course course) {
        if (course.getDescription() == null || course.getName() == null
                || course.getStartDate() == null || course.getEndDate() == null
                || course.getStartDate().isAfter(course.getEndDate())
                || !courseService.existsById(id)) {
            return ResponseEntity.badRequest().body(error);
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
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdLessons(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value.getLessons()))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.POST)
    @JsonView(View.LessonsView.class)
    public ResponseEntity<Object> postIdLessons(@PathVariable Long id, @RequestBody Lesson lesson) {
        System.out.println(lesson);
        if (!courseService.existsById(id)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())
                || !userService.existsByIdAndRole(lesson.getTeacherId(), Role.Teacher.name())) {
            return ResponseEntity.badRequest().body(error);
        }
        lesson.setCourseId(id);
        lessonService.save(lesson);
        return ResponseEntity.ok(Collections.singletonMap("lesson", lesson));
    }

    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.PUT)
    @JsonView(View.LessonsView.class)
    public ResponseEntity<Object> putIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId,
                                                 @RequestBody Lesson lesson) {
        System.out.println(lesson);
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())
                || !userService.existsByIdAndRole(lesson.getTeacherId(), Role.Teacher.name())) {
            return ResponseEntity.badRequest().body(error);
        }
        lesson.setId(lessonId);
        lesson.setCourseId(id);
        lessonService.save(lesson);
        return ResponseEntity.ok(Collections.singletonMap("lesson", lesson));
    }

    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.DELETE)
    @JsonView(View.LessonsView.class)
    public ResponseEntity<Object> deleteIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId) {
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)) {
            return ResponseEntity.badRequest().body(error);
        }
        lessonService.deleteById(lessonId);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(path = "/{id}/students", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdStudents(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value.getStudents()))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }


}
