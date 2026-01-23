package dd.task.device.manager.application.device.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeviceUpdateRequest(
        @Schema(description = "Device name") String name,
        @Schema(description = "Device brand") String brand
) {
}
