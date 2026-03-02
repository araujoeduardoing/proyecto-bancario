package liq_msa_bp_customer.infrastructure.exception;

import liq_msa_bp_customer.domain.ApiErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeFormatter;

@Getter
@ControllerAdvice
@Slf4j
public class HandlerCustomerException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formatDateTime = formatter.format(java.time.LocalDateTime.now());
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CustomerNotFoundException ex) {
        log.error("CustomerNotFoundException: {}", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Customer Not Found")
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
}
