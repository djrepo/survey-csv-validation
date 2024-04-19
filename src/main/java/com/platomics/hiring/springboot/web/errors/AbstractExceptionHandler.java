package com.platomics.hiring.springboot.web.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.platomics.hiring.springboot.web.errors.PlatomicsExceptionHandler.CONTACT_SUPPORT;


public abstract class AbstractExceptionHandler extends ResponseEntityExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(AbstractExceptionHandler.class);

    private static final String COLON = ": ";
    private static final String EMPTY_STRING = "";

    protected ResponseEntity<PlatomicsErrorResponse> handle(Exception e, String altMsg) {
        return handle(e, altMsg, getHttpStatus(e));
    }

    protected ResponseEntity<PlatomicsErrorResponse> handle(Exception e, String altMsg, HttpStatus httpStatus) {
        String msg = e.getMessage();
        if (msg == null || msg.isEmpty()) msg = altMsg;
        return handle(msg, e, httpStatus);
    }

    protected ResponseEntity<PlatomicsErrorResponse> handle(Exception e, String altMsg, HttpStatus httpStatus, HttpHeaders httpHeaders) {
        String msg = e.getMessage();
        if (msg == null || msg.isEmpty()) msg = altMsg;
        return handle(msg, e, httpStatus, httpHeaders);
    }

    protected ResponseEntity<PlatomicsErrorResponse> handle(String msg, Exception e) {
        return handle(msg, e, getHttpStatus(e));
    }

    protected ResponseEntity<PlatomicsErrorResponse> handle(String msg, final Exception e, final HttpStatus httpStatus) {
        String name = e.getClass().getSimpleName();
        logger.info(name + COLON + msg.replace(CONTACT_SUPPORT, EMPTY_STRING));
        /*if (e instanceof CaratProException && !msg.contains(CONTACT_SUPPORT)) {
            msg += CONTACT_SUPPORT;
        }*/
        PlatomicsErrorResponse platomicsErrorResponse = new PlatomicsErrorResponse(name, msg, httpStatus);
        return ResponseEntity.status(httpStatus)
                .body(platomicsErrorResponse);
    }

    protected ResponseEntity<PlatomicsErrorResponse> handle(String msg, final Exception e, final HttpStatus httpStatus, final HttpHeaders httpHeaders) {
        String name = e.getClass().getSimpleName();
        logger.info(name + COLON + msg.replace(CONTACT_SUPPORT, EMPTY_STRING));
        /*if (e instanceof CaratProException && !msg.contains(CONTACT_SUPPORT)) {
            msg += CONTACT_SUPPORT;
        }*/
        PlatomicsErrorResponse platomicsErrorResponse = new PlatomicsErrorResponse(name, msg, httpStatus);
        return ResponseEntity.status(httpStatus)
                .headers(httpHeaders)
                .body(platomicsErrorResponse);
    }

    private HttpStatus getHttpStatus(Exception e) {
        for (Object anno : e.getClass().getAnnotations()) {
            if (anno instanceof ResponseStatus) {
                return ((ResponseStatus) anno).value();
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
