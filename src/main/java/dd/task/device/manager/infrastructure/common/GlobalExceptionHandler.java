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
    private final static String MODIFICATION_FAILED_MESSAGE = "Modification failed";
    private final static String DEVICE_NOT_FOUND_MESSAGE = "Device not found";
    private final static String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(VALIDATION_FAILED_MESSAGE));
    }

    @ExceptionHandler(DeviceModificationException.class)
    public ResponseEntity<ErrorResponse> handleException(final DeviceModificationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(MODIFICATION_FAILED_MESSAGE));
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final DeviceNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(DEVICE_NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE));
    }

    public record ErrorResponse(
            String message
    ) {
    }
}


