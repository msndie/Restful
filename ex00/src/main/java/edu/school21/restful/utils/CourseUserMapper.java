package edu.school21.restful.utils;

import edu.school21.restful.dto.CourseUserDto;
import edu.school21.restful.model.User;
import org.springframework.stereotype.Component;

@Component
public class CourseUserMapper {
    public CourseUserDto toDto(User user) {
        return new CourseUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }
}
