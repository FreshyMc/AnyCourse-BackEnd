package xyz.anycourse.app.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import xyz.anycourse.app.exception.ForbiddenActionException;
import xyz.anycourse.app.exception.StorageException;
import xyz.anycourse.app.exception.UnknownRoleException;
import xyz.anycourse.app.exception.UserAlreadyExistsException;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        Map<String, Object> response = mapError(ex);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(RuntimeException ex, WebRequest request) {
        Map<String, Object> response = mapError(ex);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnknownRoleException.class)
    public ResponseEntity<Object> handleUnknownRole(RuntimeException ex, WebRequest request) {
        Map<String, Object> response = mapError(ex);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = StorageException.class)
    public ResponseEntity<Object> handleStorageException(StorageException ex, WebRequest request) {
        Map<String, Object> response = mapError(ex);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ForbiddenActionException.class)
    public ResponseEntity<Object> handleForbiddenActionException(ForbiddenActionException ex, WebRequest request) {
        Map<String, Object> response = mapError(ex);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        Map<String, Set<String>> errors = fieldErrors.stream()
                .collect(
                        Collectors.groupingBy(FieldError::getField,
                                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet()))
                );

        return new ResponseEntity<>(errors.isEmpty() ? ex : errors, headers, status);
    }

    private Map<String, Object> mapError(Exception ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("responseAt", new Date());

        return errors;
    }
}
