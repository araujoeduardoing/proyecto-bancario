package liq_msa_bp_account.infrastructure.exception;

import liq_msa_bp_account.domain.ApiErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Getter
@ControllerAdvice
@Slf4j
public class HandlerAccountException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formatDateTime = formatter.format(java.time.LocalDateTime.now());
    
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(AccountNotFoundException ex) {
        log.error("AccountNotFoundException: {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Account Not Found")
                .message(ex.getMessage())
                .detailMessage(ex.getLocalizedMessage())
                .dateTimeException(formatDateTime)
                .build();
        return ResponseEntity.status(404).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .detailMessage(ex.getLocalizedMessage())
                .dateTimeException(formatDateTime)
                .build();
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        String validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.error("MethodArgumentNotValidException: {}", validationErrors);
        
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("La solicitud fallo.")
                .detailMessage(validationErrors)
                .dateTimeException(formatDateTime)
                .build();
                
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
