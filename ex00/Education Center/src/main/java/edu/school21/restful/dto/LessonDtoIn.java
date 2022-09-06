package edu.school21.restful.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.utils.DayOfWeekDeserializer;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
public class LessonDtoIn {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    @JsonDeserialize(using = DayOfWeekDeserializer.class)
    private DayOfWeek dayOfWeek;
    private Long teacherId;
}
