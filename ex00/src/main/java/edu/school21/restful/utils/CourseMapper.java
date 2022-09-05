package edu.school21.restful.utils;

import edu.school21.restful.dto.CourseDtoIn;
import edu.school21.restful.dto.CourseDtoOut;
import edu.school21.restful.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDtoOut toDto(Course course) {
        return new CourseDtoOut(course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getName(),
                course.getDescription());
    }

    public Course toDomain(CourseDtoIn courseDtoIn) {
        Course course = new Course();
        course.setDescription(courseDtoIn.getDescription());
        course.setName(courseDtoIn.getName());
        course.setStartDate(courseDtoIn.getStartDate());
        course.setEndDate(courseDtoIn.getEndDate());
        return course;
    }
}
