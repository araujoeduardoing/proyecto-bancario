package liq_msa_bp_movements.infrastructure.exception;

public class MovementNotFoundException extends RuntimeException {
    
    public MovementNotFoundException(String message) {
        super(message);
    }
    
    public MovementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}