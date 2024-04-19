package com.platomics.hiring.springboot.web.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.platomics.hiring.springboot.web.errors.PlatomicsExceptionHandler.CONTACT_SUPPORT;

@NoArgsConstructor
@Getter
@Setter
public class CsvSummaryValidationErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();

    private String type;
    private List<String> errors;
    private String support;
    private int status;

    public CsvSummaryValidationErrorResponse(String name, List<String> msgs, HttpStatus httpStatus) {
        type = name;
        errors = msgs;
        support = CONTACT_SUPPORT;
        status = httpStatus.value();
    }

}
