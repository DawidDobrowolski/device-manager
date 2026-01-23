package dd.task.device.manager.application.device.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DeviceCreateRequest(
        @Schema(description = "Device name") @NotBlank String name,
        @Schema(description = "Device brand")  @NotBlank String brand
) {
}
