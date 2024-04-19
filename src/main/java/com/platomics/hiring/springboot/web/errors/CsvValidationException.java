package com.platomics.hiring.springboot.web.errors;

public class CsvValidationException extends RuntimeException {
    public CsvValidationException(String msg) {
        super(msg);
    }
}
