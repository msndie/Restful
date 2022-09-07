package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@ToString
@JsonTypeName("user")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CourseUserDto {
    private Long id;
    private String firstName;
    private String lastName;
}
