package edu.school21.restful.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.school21.restful.utils.View;

public class BadRequest {
    private static volatile BadRequest instance;
    @JsonView(View.LessonsView.class)
    private final int status;
    @JsonView(View.LessonsView.class)
    private final String message;

    private BadRequest() {
        this.status = 400;
        this.message = "Bad request";
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static BadRequest getInstance() {
        BadRequest localInstance = instance;
        if (localInstance == null) {
            synchronized (BadRequest.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BadRequest();
                }
            }
        }
        return localInstance;
    }
}
