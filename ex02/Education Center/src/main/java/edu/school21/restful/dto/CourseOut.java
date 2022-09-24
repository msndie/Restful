package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.school21.restful.models.CourseState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CourseOut {
    private String name;
    private String description;
    private CourseState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
