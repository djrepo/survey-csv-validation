package com.platomics.hiring.springboot.web.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.platomics.hiring.springboot.web.errors.PlatomicsExceptionHandler.CONTACT_SUPPORT;


public class PlatomicsErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final String type;
    private final String error;
    private final String support;
    private final int status;

    public PlatomicsErrorResponse(String name, String msg, HttpStatus httpStatus) {
        type = name;
        error = msg;
        support = !msg.contains(CONTACT_SUPPORT) ? "" : CONTACT_SUPPORT.trim().replace(".", "");
        status = httpStatus.value();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getError() {
        return error;
    }

    public String getSupport() {
        return support;
    }

    public int getStatus() {
        return status;
    }
}
