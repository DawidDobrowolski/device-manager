package dd.task.device.manager.application.device.model;

import dd.task.device.manager.domain.device.model.State;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceResponse(
        UUID uuid,
        String name,
        String brand,
        State state,
        LocalDateTime creationTime
) {
}
