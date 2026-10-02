package com.example.STUDENT.exception;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentNotFound(
            DepartmentNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "Department not found");
    }

    @ExceptionHandler(DuplicateDepartmentCodeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateDepartmentCode(
            DuplicateDepartmentCodeException exception) {
        return error(HttpStatus.CONFLICT, "Department code already exists");
    }

    @ExceptionHandler(DepartmentInUseException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentInUse(
            DepartmentInUseException exception) {
        return error(HttpStatus.CONFLICT, "Department has students and cannot be deleted");
    }

    @ExceptionHandler(InvalidStudentDepartmentException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStudentDepartment(
            InvalidStudentDepartmentException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFound(
            StudentNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, "Student not found");
    }

    @ExceptionHandler(DuplicateRegisterNumberException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateRegisterNumber(
            DuplicateRegisterNumberException exception) {
        return error(HttpStatus.CONFLICT, "Register number already exists");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "Invalid value"
                                : fieldError.getDefaultMessage(),
                        (firstMessage, ignored) -> firstMessage,
                        TreeMap::new));

        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation failed");
        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableRequestBody(
            HttpMessageNotReadableException exception) {
        return error(HttpStatus.BAD_REQUEST,
                "Invalid or missing JSON request body. Check the JSON syntax and ensure values "
                        + "match the expected field types.");
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
