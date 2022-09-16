package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class IdRequest {

    @JsonAlias({"teacherId", "studentId"})
    @Positive
    @NotNull
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
