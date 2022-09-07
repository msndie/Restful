package edu.school21.restful.utils;

import edu.school21.restful.dto.*;
import edu.school21.restful.model.Course;
import edu.school21.restful.model.Lesson;
import edu.school21.restful.model.User;

public class MappingUtils {
    public static CourseDto courseToDto(Course course) {
        return new CourseDto(course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getName(),
                course.getDescription());
    }

    public static Course courseToDomain(CourseDto courseDtoIn) {
        Course course = new Course();
        course.setDescription(courseDtoIn.getDescription());
        course.setName(courseDtoIn.getName());
        course.setStartDate(courseDtoIn.getStartDate());
        course.setEndDate(courseDtoIn.getEndDate());
        return course;
    }

    public static CourseUserDto courseUserToDto(User user) {
        return new CourseUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public static LessonDtoOut lessonToDto(Lesson lesson) {
        return new LessonDtoOut(lesson.getId(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getDayOfWeek(),
                courseUserToDto(lesson.getTeacher()));
    }

    public static Lesson lessonToDomain(LessonDtoIn lessonDtoIn) {
        Lesson lesson = new Lesson();
        lesson.setStartTime(lessonDtoIn.getStartTime());
        lesson.setEndTime(lessonDtoIn.getEndTime());
        lesson.setDayOfWeek(lessonDtoIn.getDayOfWeek());
        return lesson;
    }

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    public static User userToDomain(UserDto userDto) {
        User user = new User();
        user.setRole(userDto.getRole());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setId(userDto.getId());
        return user;
    }
}
