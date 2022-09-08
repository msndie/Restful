package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.utils.DayOfWeekDeserializer;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
@ApiModel(value = "lesson")
public class LessonDtoIn {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    @JsonDeserialize(using = DayOfWeekDeserializer.class)
    private DayOfWeek dayOfWeek;
    private Long teacherId;
}