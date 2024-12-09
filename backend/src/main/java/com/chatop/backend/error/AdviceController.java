package com.chatop.backend.error;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.chatop.backend.dto.ErrorResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<HashMap<String, String>> handleValidationException(MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        BindingResult result = exception.getBindingResult();
        var errorsList = result.getAllErrors();
        HashMap<String, String> validationErrors = new HashMap<>();

        for (int i = 0; i < errorsList.size(); i++) {
            var error = (FieldError) errorsList.get(i);
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(validationErrors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> resourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        ErrorResponseDto message = new ErrorResponseDto(ex.getMessage());

        return new ResponseEntity<ErrorResponseDto>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponseDto message = new ErrorResponseDto(ex.getMessage());

        return new ResponseEntity<ErrorResponseDto>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
