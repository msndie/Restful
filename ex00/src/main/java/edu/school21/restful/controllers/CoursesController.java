package edu.school21.restful.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.school21.restful.dto.CourseDtoIn;
import edu.school21.restful.dto.LessonDtoIn;
import edu.school21.restful.dto.CourseUserDto;
import edu.school21.restful.model.*;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.CourseMapper;
import edu.school21.restful.utils.LessonMapper;
import edu.school21.restful.utils.CourseUserMapper;
import edu.school21.restful.utils.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final Map<String, Object> error;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;
    private final CourseUserMapper userMapper;

    @Autowired
    public CoursesController(CourseService courseService,
                             LessonService lessonService,
                             UserService userService,
                             CourseMapper courseMapper,
                             LessonMapper lessonMapper,
                             CourseUserMapper userMapper) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
        this.userMapper = userMapper;
        this.error = Collections.singletonMap("error", BadRequest.getInstance());
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(courseService
                .findAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody CourseDtoIn request) {
        System.out.println(request);
        if (request.getDescription() == null || request.getName() == null
            || request.getStartDate() == null || request.getEndDate() == null
            || request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity.badRequest().body(error);
        }
        Course course = courseMapper.toDomain(request);
        courseService.save(course);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(Collections.singletonMap("course", courseMapper.toDto(value))))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> putId(@PathVariable Long id, @RequestBody CourseDtoIn course) {
        if (course.getDescription() == null || course.getName() == null
                || course.getStartDate() == null || course.getEndDate() == null
                || course.getStartDate().isAfter(course.getEndDate())
                || !courseService.existsById(id)) {
            return ResponseEntity.badRequest().body(error);
        }
        Course domain = courseMapper.toDomain(course);
        domain.setId(id);
        courseService.save(domain);
        return ResponseEntity.ok(Collections.singletonMap("course", courseMapper.toDto(domain)));
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
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value
                        .getLessons()
                        .stream()
                        .map(lessonMapper::toDto)
                        .collect(Collectors.toList())))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.POST)
    @JsonView(View.LessonsView.class)
    public ResponseEntity<Object> postIdLessons(@PathVariable Long id, @RequestBody LessonDtoIn lesson) {
        System.out.println(lesson);
        if (!courseService.existsById(id)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())
                || !userService.existsByIdAndRole(lesson.getTeacherId(), Role.TEACHER)) {
            return ResponseEntity.badRequest().body(error);
        }
        Lesson domain = lessonMapper.toDomain(lesson);
        domain.setCourseId(id);
        domain.setTeacher(userService.findById(lesson.getTeacherId()).get());
        lessonService.save(domain);
        return ResponseEntity.ok(Collections.singletonMap("lesson", lessonMapper.toDto(domain)));
    }

    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.PUT)
    @JsonView(View.LessonsView.class)
    public ResponseEntity<Object> putIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId,
                                                 @RequestBody LessonDtoIn lesson) {
        System.out.println(lesson);
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())
                || !userService.existsByIdAndRole(lesson.getTeacherId(), Role.TEACHER)) {
            return ResponseEntity.badRequest().body(error);
        }
        Lesson domain = lessonMapper.toDomain(lesson);
        domain.setId(lessonId);
        domain.setCourseId(id);
        lessonService.save(domain);
        return ResponseEntity.ok(Collections.singletonMap("lesson", lessonMapper.toDto(domain)));
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
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value
                        .getStudents()
                        .stream()
                        .map(user -> new CourseUserDto(user.getId(), user.getFirstName(), user.getLastName()))))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}/students", method = RequestMethod.POST)
    public ResponseEntity<Object> postIdStudents(@PathVariable Long id, @RequestParam Long userId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(userId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().add(user.get());
            courseService.save(course.get());
            return ResponseEntity.ok(Collections.singletonMap("student", userMapper.toDto(user.get())));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/students/{studentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteIdStudentsId(@PathVariable Long id,
                                                     @PathVariable Long studentId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().remove(user.get());
            courseService.save(course.get());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdTeachers(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        if (course.isPresent()) {
            System.out.println(course.get().getTeachers());
        }
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(value
                        .getTeachers()
                        .stream()
                        .map(user -> new CourseUserDto(user.getId(), user.getFirstName(), user.getLastName()))
                        .collect(Collectors.toList())))
                .orElseGet(() -> ResponseEntity.badRequest().body(error));
    }

    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.POST)
    public ResponseEntity<Object> postIdTeachers(@PathVariable Long id, @RequestBody Long userId) {
        if (courseService.existsById(id)
                && userService.existsByIdAndRole(userId, Role.TEACHER)) {
            return ResponseEntity.ok(Collections.singletonMap("teacher", userMapper.toDto(userService.findById(userId).get())));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/teachers/{teacherId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteIdTeachersId(@PathVariable Long id,
                                                     @PathVariable Long teacherId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER) {
            course.get().getTeachers().remove(user.get());
            courseService.save(course.get());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(error);
    }
}
