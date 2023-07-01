package kz.gaziz.java.exam.handler;

import kz.gaziz.java.exam.exception.ThrottlingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ThrottlingExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ThrottlingException.class})
    protected ResponseEntity<Object> handleThrottlingException(ThrottlingException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), HttpHeaders.EMPTY, HttpStatus.BAD_GATEWAY, request);
    }
}
