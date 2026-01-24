package dd.task.device.manager.application.device.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * State update is not implemented yet and can be added later,
 * including validation of allowed transitions between states.
 */
public record DeviceUpdateRequest(
        @Schema(description = "Device name") String name,
        @Schema(description = "Device brand") String brand
) {
}
