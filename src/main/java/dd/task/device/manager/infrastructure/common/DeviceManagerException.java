package dd.task.device.manager.infrastructure.common;

public class DeviceManagerException extends RuntimeException {

    public DeviceManagerException(Throwable cause) {
        super(cause);
    }

    public DeviceManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
