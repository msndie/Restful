package edu.school21.restful.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.restful.model.Role;
import edu.school21.restful.utils.RoleDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;
}
