package edu.school21.restful.controllers;

import edu.school21.restful.dto.*;
import edu.school21.restful.exception.BadRequestException;
import edu.school21.restful.model.*;
import edu.school21.restful.services.CourseService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.MappingUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CoursesController {

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
    }

    @GetMapping(produces = "application/json")
    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    public ResponseEntity<List<CourseDto>> get(@RequestParam(required = false, value = "page") Integer page,
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
                throw new BadRequestException();
            }
        }
        return ResponseEntity.ok(courseService
                .findAll()
                .stream()
                .map(MappingUtils::courseToDto)
                .collect(Collectors.toList()));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @PostMapping(produces = "application/json")
    public ResponseEntity<CourseDto> post(@RequestBody CourseDto request) {
        if (request.getDescription() == null || request.getName() == null
            || request.getStartDate() == null || request.getEndDate() == null
            || request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException();
        }
        Course course = MappingUtils.courseToDomain(request);
        courseService.save(course);
        return ResponseEntity.ok(MappingUtils.courseToDto(course));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CourseDto> getId(@PathVariable Long id) {
        Optional<Course> course = courseService.getById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(MappingUtils.courseToDto(course.get()));
        } else {
            throw new BadRequestException();
        }
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<CourseDto> putId(@PathVariable Long id, @RequestBody CourseDto course) {
        if (course.getDescription() == null || course.getName() == null
                || course.getStartDate() == null || course.getEndDate() == null
                || course.getStartDate().isAfter(course.getEndDate())
                || !courseService.existsById(id)) {
            throw new BadRequestException();
        }
        Course domain = MappingUtils.courseToDomain(course);
        domain.setId(id);
        courseService.update(domain);
        return ResponseEntity.ok(MappingUtils.courseToDto(domain));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteId(@PathVariable Long id) {
        if (courseService.existsById(id)) {
            courseService.deleteById(id);
            return ResponseEntity.ok(null);
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<LessonDtoOut>> getIdLessons(@PathVariable Long id,
                                               @RequestParam(required = false, value = "page") Integer page,
                                               @RequestParam(required = false, value = "size") Integer size) {
        if (courseService.existsById(id)) {
            if (page != null && size != null) {
                if (page >= 0 && size > 0) {
                    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                    return ResponseEntity.ok(lessonService.findAllByCourseId(id, pageable)
                            .stream()
                            .map(MappingUtils::lessonToDto)
                            .collect(Collectors.toList()));
                } else {
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(lessonService.findAll()
                    .stream()
                    .map(MappingUtils::lessonToDto)
                    .sorted(Comparator.comparingLong(LessonDtoOut::getId))
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/lessons", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<LessonDtoOut> postIdLessons(@PathVariable Long id, @RequestBody LessonDtoIn lesson) {
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
                return ResponseEntity.ok(MappingUtils.lessonToDto(domain));
            }
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<LessonDtoOut> putIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId,
                                                 @RequestBody LessonDtoIn lesson) {
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)
                || lesson.getTeacherId() == null || lesson.getDayOfWeek() == null
                || lesson.getStartTime() == null || lesson.getEndTime() == null
                || lesson.getStartTime().isAfter(lesson.getEndTime())) {
            throw new BadRequestException();
        }
        Optional<User> user = userService.findById(lesson.getTeacherId());
        if (!user.isPresent()) {
            throw new BadRequestException();
        }
        Lesson domain = MappingUtils.lessonToDomain(lesson);
        domain.setTeacher(user.get());
        domain.setId(lessonId);
        domain.setCourseId(id);
        lessonService.update(domain);
        return ResponseEntity.ok(MappingUtils.lessonToDto(domain));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/lessons/{lessonId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteIdLessonsId(@PathVariable Long id,
                                                 @PathVariable Long lessonId) {
        if (!courseService.existsById(id) || !lessonService.existsById(lessonId)) {
            throw new BadRequestException();
        }
        lessonService.deleteById(lessonId);
        return ResponseEntity.ok(null);
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/students", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CourseUserDto>> getIdStudents(@PathVariable Long id,
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
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(userService.findAllStudentsByCourseId(id)
                    .stream()
                    .map(MappingUtils::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/students", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<CourseUserDto> postIdStudents(@PathVariable Long id, @RequestBody Id studentId) {
        if (studentId.getId() == null) {
            throw new BadRequestException();
        }
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().add(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(MappingUtils.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/students/{studentId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteIdStudentsId(@PathVariable Long id,
                                                     @PathVariable Long studentId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(studentId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.STUDENT) {
            course.get().getStudents().remove(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(null);
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CourseUserDto>> getIdTeachers(@PathVariable Long id,
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
                    throw new BadRequestException();
                }
            }
            return ResponseEntity.ok(userService.findAllTeachersByCourseId(id)
                    .stream()
                    .map(MappingUtils::courseUserToDto)
                    .collect(Collectors.toList()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/teachers", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<CourseUserDto> postIdTeachers(@PathVariable Long id, @RequestBody Id teacherId) {
        if (teacherId.getId() == null) {
            throw new BadRequestException();
        }
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId.getId());
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER) {
            course.get().getTeachers().add(user.get());
            courseService.update(course.get());
            return ResponseEntity
                    .ok(MappingUtils.courseUserToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{id}/teachers/{teacherId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteIdTeachersId(@PathVariable Long id,
                                                     @PathVariable Long teacherId) {
        Optional<Course> course = courseService.getById(id);
        Optional<User> user = userService.findById(teacherId);
        if (course.isPresent() && user.isPresent() && user.get().getRole() == Role.TEACHER) {
            course.get().getTeachers().remove(user.get());
            courseService.update(course.get());
            return ResponseEntity.ok(null);
        }
        throw new BadRequestException();
    }
}
