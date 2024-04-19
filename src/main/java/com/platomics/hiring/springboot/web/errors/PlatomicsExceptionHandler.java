package com.platomics.hiring.springboot.web.errors;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@ControllerAdvice
public class PlatomicsExceptionHandler extends AbstractExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlatomicsExceptionHandler.class);
    public static final String CONTACT_SUPPORT = " Please contact Support.";
    private static final String WRONG_REQUEST = "Wrong request!";
    private static final String DEFAULT_UNTREATED_CODE_EXCEPTION = "Untreated code exception error." + CONTACT_SUPPORT;



    /* common exceptions */
    @ExceptionHandler({NullPointerException.class, IndexOutOfBoundsException.class, ClassCastException.class, UnsupportedOperationException.class})
    public ResponseEntity<PlatomicsErrorResponse> handleUntreatedCodeException(RuntimeException e, WebRequest request) {
        logger.error("handleUntreatedCodeException received",e);
        return handle(DEFAULT_UNTREATED_CODE_EXCEPTION, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* https://stackoverflow.com/a/36693933 */
    /* https://stackoverflow.com/a/60709387 */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Object exceptionHandler(IOException e) {
        String rootCause = ExceptionUtils.getRootCauseMessage(e);
        if (StringUtils.containsIgnoreCase(rootCause, "Broken pipe") || StringUtils.containsIgnoreCase(rootCause, "Stream is closed") || StringUtils.containsIgnoreCase(rootCause, "IOException: An established connection was aborted by the software in your host machine")) {
            return null;  // socket is closed, cannot return any response
        } else {
            logger.error("exceptionHandler received",e);
            return handle(DEFAULT_UNTREATED_CODE_EXCEPTION, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<PlatomicsErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return handle(e, "File too large!", HttpStatus.EXPECTATION_FAILED);
    }

}
