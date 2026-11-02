package com.ensa.notes.exceptions;

import com.ensa.notes.models.HttpResponse;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request)
    {
    log.error(ex.getMessage(), ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldErrorMessage = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" , "));

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().format(FORMATTER))
                        .statusCode(status.value())
                        .reason(fieldErrorMessage)
                      //  .developerMessage(ex.getMessage())
                        .build()
                , status
        );
    }


    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {

        log.error(ex.getMessage(), ex);

        HttpStatus resolvedStatus = HttpStatus.resolve(statusCode.value());
        HttpStatus httpStatus = (resolvedStatus != null) ? resolvedStatus : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(httpStatus.is5xxServerError() ? "An Internal Error occurred !!" : httpStatus.getReasonPhrase())
                        .developerMessage(ex.getMessage())
                        .statusCode(statusCode.value())
                        .httpStatus(httpStatus)
                        .timeStamp(LocalDateTime.now().format(FORMATTER))
                        .build(),
                headers,
                statusCode
        );
    }


    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<HttpResponse<Object>> handleNoteNotFoundException(NoteNotFoundException ex) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(ex.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .timeStamp(LocalDateTime.now().format(FORMATTER))
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

}
