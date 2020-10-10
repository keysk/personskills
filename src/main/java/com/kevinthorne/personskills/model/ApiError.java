package com.kevinthorne.personskills.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ApiError {

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;


    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String message) {
        super();
        this.status = status;
        this.message = message;
    }

    //@ApiModelProperty(example = "BAD_REQUEST", value = "Error Status")
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    //@ApiModelProperty(example = "Validation Failed", value = "The summary of the error that has occurred.")
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
