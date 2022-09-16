package edu.school21.restful.model.dto;

import edu.school21.restful.model.Course;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

//@Projection(name = "courseDto", types = Course.class)
public interface CourseDto {
    LocalDate startDate();
    LocalDate endDate();
    String name();
    String description();
}
