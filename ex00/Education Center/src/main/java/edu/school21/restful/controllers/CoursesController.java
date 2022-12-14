package edu.school21.restful.controllers;

import edu.school21.restful.dto.*;
import edu.school21.restful.exception.BadRequestException;
import edu.school21.restful.model.*;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.DtoMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    @Autowired
    public CoursesController(CourseService courseService,
                             LessonService lessonService,
                             UserService userService,
                             DtoMapper dtoMapper) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @ApiOperation(value = "Get all courses")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CourseResponse>> get(@RequestParam(required = false, value = "page") Integer page,
                                                    @RequestParam(required = false, value = "size") Integer size) {
        if (page != null && size != null) {
            if (page >= 0 && size > 0) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                return ResponseEntity.ok(courseService
                        .findAll(pageable)
                        .stream()
                        .map(dtoMapper::courseToDto)
                        .collect(Collectors.toList()));
            } else {
                throw new BadRequestException();
            }
        }
        return ResponseEntity.ok(courseService
                .findAll()
                .stream()
                .map(dtoMapper::courseToDto)
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Add new course")
    @PostMapping(produces = "application/json")
    public ResponseEntity<CourseResponse> post(@Valid @RequestBody CourseRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException();
        }
        Course course = dtoMapper.courseToDomain(request);
        courseService.save(course);
        return ResponseEntity.ok(dtoMapper.courseToDto(course));
    }

    @ApiOperation(value = "Get course by ID")
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CourseResponse> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(dtoMapper.courseToDto(course.get()));
        } else {
            throw new BadRequestException();
        }
    }

    @ApiOperation(value = "Change course by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<CourseResponse> putId(@PathVariable Long id,
                                                @Valid @RequestBody CourseRequest course) {
        if (course.getStartDate().isAfter(course.getEndDate()) || !courseService.existsById(id)) {
            throw new BadRequestException();
        }
        Course domain = dtoMapper.courseToDomain(course);
        domain.setId(id);
        courseService.update(domain);
        return ResponseEntity.ok(dtoMapper.courseToDto(domain));
    }

    @ApiOperation(value = "Delete course by ID")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<CourseResponse> deleteId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        if (course.isPresent()) {
            courseService.deleteById(id);
            return ResponseEntity.ok(dtoMapper.courseToDto(course.get()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Get all lessons by course ID")
    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<LessonResponse>> getIdLessons(@PathVariable Long id,
                                                             @RequestParam(required = false, value = "page") Integer page,
                                                             @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(lessonService.findAllByCourseId(id, pageable)
                            .stream()
                            .map(dtoMapper::lessonToDto)
                            .collect(Collectors.toList()));
                } else {
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(lessonService.findAllByCourseId(id, Pageable.unpaged())
                    .stream()
                    .map(dtoMapper::lessonToDto)
                    .sorted(Comparator.comparingLong(LessonResponse::getId))
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Add new lesson to course")
    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<LessonResponse> postIdLessons(@PathVariable Long id,
                                                        @Valid @RequestBody LessonRequest lesson) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(lesson.getTeacherId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER
                && course.get().getTeachers().contains(user.get())
                && lesson.getStartTime().isBefore(lesson.getEndTime())) {
            Lesson domain = dtoMapper.lessonToDomain(lesson);
            domain.setCourseId(id);
            domain.setTeacher(user.get());
            lessonService.save(domain);
            return ResponseEntity.ok(dtoMapper.lessonToDto(domain));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Change lesson in selected course by lesson ID")
    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<LessonResponse> putIdLessonsId(@PathVariable Long id,
                                                         @PathVariable Long lessonId,
                                                         @Valid @RequestBody LessonRequest lesson) {
        Optional<Course> course = courseService.getById(id);
        Optional<Lesson> lessonOptional = lessonService.findById(lessonId);
        if (!course.isPresent() || !lessonOptional.isPresent()
                || lesson.getStartTime().isAfter(lesson.getEndTime())) {
            throw new BadRequestException();
        }
        Optional<User> user = userService.findById(lesson.getTeacherId());
        if (!user.isPresent() || !course.get().getTeachers().contains(user.get())) {
            throw new BadRequestException();
        }
        Lesson domain = dtoMapper.lessonToDomain(lesson);
        domain.setTeacher(user.get());
        domain.setId(lessonId);
        domain.setCourseId(id);
        lessonService.update(domain);
        return ResponseEntity.ok(dtoMapper.lessonToDto(domain));
    }

    @ApiOperation(value = "Delete lesson in selected course by lesson ID")
    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<LessonResponse> deleteIdLessonsId(@PathVariable Long id,
                                                    @PathVariable Long lessonId) {
        Optional<Lesson> lesson = lessonService.findByIdAndCourseId(lessonId, id);
        if (!lesson.isPresent()) {
            throw new BadRequestException();
        }
        lessonService.deleteById(lessonId);
        return ResponseEntity.ok(dtoMapper.lessonToDto(lesson.get()));
    }

    @ApiOperation(value = "Get all course students")
    @RequestMapping(path = "/{id}/students", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CourseUserResponse>> getIdStudents(@PathVariable Long id,
                                                @RequestParam(required = false, value = "page") Integer page,
                                                @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(userService.findAllStudentsByCourseId(id, pageable)
                            .stream()
                            .map(dtoMapper::courseUserToDto)
                            .collect(Collectors.toList()));
                } else {
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(userService.findAllStudentsByCourseId(id)
                    .stream()
                    .map(dtoMapper::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Add new student to course")
    @RequestMapping(path = "/{id}/students", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<CourseUserResponse> postIdStudents(@PathVariable Long id,
                                                             @Valid @RequestBody IdRequest studentId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().add(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(dtoMapper.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Delete student from course")
    @RequestMapping(path = "/{id}/students/{studentId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<CourseUserResponse> deleteIdStudentsId(@PathVariable Long id,
                                                     @PathVariable Long studentId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT
                && course.get().getStudents().contains(user.get())) {
            course.get().getStudents().remove(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(dtoMapper.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Get all course teachers")
    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CourseUserResponse>> getIdTeachers(@PathVariable Long id,
                                                @RequestParam(required = false, value = "page") Integer page,
                                                @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(userService.findAllTeachersByCourseId(id, pageable)
                            .stream()
                            .map(dtoMapper::courseUserToDto)
                            .collect(Collectors.toList()));
                } else {
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(userService.findAllTeachersByCourseId(id)
                    .stream()
                    .map(dtoMapper::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Add new teacher to course")
    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<CourseUserResponse> postIdTeachers(@PathVariable Long id,
                                                             @Valid @RequestBody IdRequest teacherId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER) {
            course.get().getTeachers().add(user.get());
            courseService.update(course.get());
            return ResponseEntity
                    .ok(dtoMapper.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Delete teacher from course")
    @RequestMapping(path = "/{id}/teachers/{teacherId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<CourseUserResponse> deleteIdTeachersId(@PathVariable Long id,
                                                     @PathVariable Long teacherId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER
                && course.get().getTeachers().contains(user.get())) {
            course.get().getTeachers().remove(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(dtoMapper.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }
}
