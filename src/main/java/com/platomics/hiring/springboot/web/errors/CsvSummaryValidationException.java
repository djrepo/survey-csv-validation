package com.platomics.hiring.springboot.web.errors;

import lombok.Getter;

import java.util.List;

@Getter
public class CsvSummaryValidationException extends RuntimeException {

    private List<String> validationErrorMsgs;

    public CsvSummaryValidationException(List<String> msgs) {
        this.validationErrorMsgs = msgs;
    }
}
