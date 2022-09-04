package edu.school21.restful.model;

public class BadRequest {
    private static volatile BadRequest instance;
    private final int status;
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
