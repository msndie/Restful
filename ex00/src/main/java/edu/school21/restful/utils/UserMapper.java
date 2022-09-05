package edu.school21.restful.utils;

import edu.school21.restful.dto.UserDto;
import edu.school21.restful.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    public User toDomain(UserDto userDto) {
        User user = new User();
        user.setRole(userDto.getRole());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setId(userDto.getId());
        return user;
    }
}
