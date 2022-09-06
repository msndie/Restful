package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Id {

    @JsonAlias({"teacherId", "studentId"})
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
