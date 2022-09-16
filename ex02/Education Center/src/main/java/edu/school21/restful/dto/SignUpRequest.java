package edu.school21.restful.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
