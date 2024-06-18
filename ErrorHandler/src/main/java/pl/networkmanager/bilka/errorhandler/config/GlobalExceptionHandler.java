package pl.networkmanager.bilka.errorhandler.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;
import pl.networkmanager.bilka.errorhandler.entity.ErrorResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomError.class)
    public final ResponseEntity<ErrorResponse> handleCustomErrors(CustomError ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        var response = new ErrorResponse(errors, ex.getCode());
        return new ResponseEntity<>(response, new HttpHeaders(), ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.add(error.getField() + " " + error.getDefaultMessage()));
        var response = new ErrorResponse(errors, "BAD_REQUEST");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        var response = new ErrorResponse(getErrorsInternalServer(errors), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        var response = new ErrorResponse(getErrorsInternalServer(errors), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private List<String> getErrorsInternalServer(List<String> errors) {
        log.error(errors.toString());
        List<String> errorsMessage = new ArrayList<>();
        errorsMessage.add("Something went wrong, please try later");
        return errorsMessage;
    }
}