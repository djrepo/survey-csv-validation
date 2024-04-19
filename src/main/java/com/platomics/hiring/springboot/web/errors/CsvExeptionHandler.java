package com.platomics.hiring.springboot.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CsvExeptionHandler extends AbstractExceptionHandler {
    private static final String WRONG_CSV = "Validation of csv failed!";
    /* Custom exceptions */
    @ExceptionHandler({CsvSummaryValidationException.class})
    public ResponseEntity<CsvSummaryValidationErrorResponse> handleCaratProException(RuntimeException e, WebRequest request) {
        CsvSummaryValidationErrorResponse csvSummaryValidationErrorResponse = new CsvSummaryValidationErrorResponse(WRONG_CSV, ((CsvSummaryValidationException)e).getValidationErrorMsgs(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(csvSummaryValidationErrorResponse);
    }
}
