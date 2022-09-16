package edu.school21.restful.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModelProperty;

@JsonTypeName("error")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class BadRequest {
    private static volatile BadRequest instance;
    @ApiModelProperty(example = "400")
    private final int status;
    @ApiModelProperty(example = "Bad request")
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
