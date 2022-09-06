package edu.school21.restful.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
public class LessonDtoOut {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private CourseUserDto teacher;
}
