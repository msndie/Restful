package edu.school21.restful.controllers;

import edu.school21.restful.dto.*;
import edu.school21.restful.model.*;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
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

    @Autowired
    public CoursesController(CourseService courseService,
                             LessonService lessonService,
                             UserService userService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.error = Collections.singletonMap("error", BadRequest.getInstance());
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = false, value = "page") Integer page,
                                      @RequestParam(required = false, value = "size") Integer size) {
        if (page != null && size != null) {
            if (page >= 0 && size > 0) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                return ResponseEntity.ok(courseService
                        .findAll(pageable)
                        .stream()
                        .map(MappingUtils::courseToDto)
                        .collect(Collectors.toList()));
            } else {
                return ResponseEntity.badRequest().body(error);
            }
        }
        return ResponseEntity.ok(courseService
                .findAll()
                .stream()
                .map(MappingUtils::courseToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody CourseDtoIn request) {
        if (request.getDescription() == null || request.getName() == null
            || request.getStartDate() == null || request.getEndDate() == null
            || request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity.badRequest().body(error);
        }
        Course course = MappingUtils.courseToDomain(request);
        courseService.save(course);
        return ResponseEntity.ok(MappingUtils.courseToDto(course));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        return course
                .<ResponseEntity<Object>>map(value -> ResponseEntity.ok(Collections.singletonMap("course", MappingUtils.courseToDto(value))))
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
        Course domain = MappingUtils.courseToDomain(course);
        domain.setId(id);
        courseService.update(domain);
        return ResponseEntity.ok(Collections.singletonMap("course", MappingUtils.courseToDto(domain)));
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
    public ResponseEntity<Object> getIdLessons(@PathVariable Long id,
                                               @RequestParam(required = false, value = "page") Integer page,
                                               @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(lessonService.findAllByCourseId(id, pageable)
                            .stream()
                            .map(MappingUtils::lessonToDto)
                            .sorted(Comparator.comparingLong(LessonDtoOut::getId))
                            .collect(Collectors.toList()));
                } else {
                    return ResponseEntity.badRequest().body(error);
                }
            }
            return ResponseEntity.ok(lessonService.findAll()
                    .stream()
                    .map(MappingUtils::lessonToDto)
                    .sorted(Comparator.comparingLong(LessonDtoOut::getId))
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.POST)
    public ResponseEntity<Object> postIdLessons(@PathVariable Long id, @RequestBody LessonDtoIn lesson) {
        if (lesson.getTeacherId() != null) {
            Optional<Course> course = courseService.getById(id);
            Optional<User> user = userService.findById(lesson.getTeacherId());
            if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER
                    && course.get().getTeachers().contains(user.get())
                    && lesson.getDayOfWeek() != null && lesson.getStartTime() != null
                    && lesson.getEndTime() != null && lesson.getStartTime().isBefore(lesson.getEndTime())) {
                Lesson domain = MappingUtils.lessonToDomain(lesson);
                domain.setCourseId(id);
                domain.setTeacher(user.get());
                lessonService.save(domain);
                return ResponseEntity.ok(Collections.singletonMap("lesson", MappingUtils.lessonToDto(domain)));
            }
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> putIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId,
                                                 @RequestBody LessonDtoIn lesson) {
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())
                || !userService.existsByIdAndRole(lesson.getTeacherId(), Role.TEACHER)) {
            return ResponseEntity.badRequest().body(error);
        }
        Lesson domain = MappingUtils.lessonToDomain(lesson);
        domain.setId(lessonId);
        domain.setCourseId(id);
        lessonService.update(domain);
        return ResponseEntity.ok(Collections.singletonMap("lesson", MappingUtils.lessonToDto(domain)));
    }

    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId) {
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)) {
            return ResponseEntity.badRequest().body(error);
        }
        lessonService.deleteById(lessonId);
        return ResponseEntity.ok(null);
    }

    @RequestMapping(path = "/{id}/students", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdStudents(@PathVariable Long id,
                                                @RequestParam(required = false, value = "page") Integer page,
                                                @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(userService.findAllStudentsByCourseId(id, pageable)
                            .stream()
                            .map(MappingUtils::courseUserToDto)
                            .collect(Collectors.toList()));
                } else {
                    return ResponseEntity.badRequest().body(error);
                }
            }
            return ResponseEntity.ok(userService.findAllStudentsByCourseId(id)
                    .stream()
                    .map(MappingUtils::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/students", method = RequestMethod.POST)
    public ResponseEntity<Object> postIdStudents(@PathVariable Long id, @RequestBody Id studentId) {
        if (studentId.getId() == null) {
            return ResponseEntity.badRequest().body(error);
        }
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().add(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(Collections.singletonMap("student", MappingUtils.courseUserToDto(user.get())));
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
            courseService.update(course.get());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.GET)
    public ResponseEntity<Object> getIdTeachers(@PathVariable Long id,
                                                @RequestParam(required = false, value = "page") Integer page,
                                                @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(userService.findAllTeachersByCourseId(id, pageable)
                            .stream()
                            .map(MappingUtils::courseUserToDto)
                            .collect(Collectors.toList()));
                } else {
                    return ResponseEntity.badRequest().body(error);
                }
            }
            return ResponseEntity.ok(userService.findAllTeachersByCourseId(id)
                    .stream()
                    .map(MappingUtils::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.POST)
    public ResponseEntity<Object> postIdTeachers(@PathVariable Long id, @RequestBody Id teacherId) {
        if (teacherId.getId() == null) {
            return ResponseEntity.badRequest().body(error);
        }
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER) {
            course.get().getTeachers().add(user.get());
            courseService.update(course.get());
            return ResponseEntity
                    .ok(Collections.singletonMap("teacher", MappingUtils.courseUserToDto(user.get())));
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
            courseService.update(course.get());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(error);
    }
}
