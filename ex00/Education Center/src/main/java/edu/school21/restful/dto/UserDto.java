package edu.school21.restful.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.model.Role;
import edu.school21.restful.utils.RoleDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;
}
