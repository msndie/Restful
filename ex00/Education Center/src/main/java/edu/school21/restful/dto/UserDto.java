package edu.school21.restful.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.model.Role;
import edu.school21.restful.utils.RoleDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @ApiModelProperty(readOnly = true)
    private Long id;
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;
}
