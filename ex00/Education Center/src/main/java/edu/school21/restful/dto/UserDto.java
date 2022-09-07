package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.model.Role;
import edu.school21.restful.utils.RoleDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@JsonTypeName("user")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class UserDto {
    @ApiModelProperty(readOnly = true)
    private Long id;
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;
}
