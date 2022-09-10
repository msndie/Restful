package edu.school21.restful.utils;

import edu.school21.restful.dto.*;
import edu.school21.restful.model.Course;
import edu.school21.restful.model.Lesson;
import edu.school21.restful.model.User;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    public CourseResponse courseToDto(Course course) {
        return new CourseResponse(course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getName(),
                course.getDescription());
    }

    public Course courseToDomain(CourseRequest courseRequest) {
        Course course = new Course();
        course.setDescription(courseRequest.getDescription());
        course.setName(courseRequest.getName());
        course.setStartDate(courseRequest.getStartDate());
        course.setEndDate(courseRequest.getEndDate());
        return course;
    }

    public CourseUserResponse courseUserToDto(User user) {
        return new CourseUserResponse(user.getId(), user.getFirstName(), user.getLastName());
    }

    public LessonResponse lessonToDto(Lesson lesson) {
        return new LessonResponse(lesson.getId(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                lesson.getDayOfWeek(),
                courseUserToDto(lesson.getTeacher()));
    }

    public Lesson lessonToDomain(LessonRequest lessonRequest) {
        Lesson lesson = new Lesson();
        lesson.setStartTime(lessonRequest.getStartTime());
        lesson.setEndTime(lessonRequest.getEndTime());
        lesson.setDayOfWeek(lessonRequest.getDayOfWeek());
        return lesson;
    }

    public UserResponse userToDto(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    public User userToDomain(UserRequest userRequest) {
        User user = new User();
        user.setRole(userRequest.getRole());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        return user;
    }
}
