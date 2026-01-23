package dd.task.device.manager.infrastructure.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final static String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private final static String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(VALIDATION_FAILED_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(INTERNAL_SERVER_ERROR_MESSAGE);
    }
}


