package dd.task.device.manager.interfaces;

import dd.task.device.manager.application.device.DeviceApplicationService;
import dd.task.device.manager.application.device.model.DeviceCreateRequest;
import dd.task.device.manager.application.device.model.DeviceResponse;
import dd.task.device.manager.application.device.model.DeviceUpdateRequest;
import dd.task.device.manager.infrastructure.common.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("device-management/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceApplicationService service;

    @Operation(summary = "Create device")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Device created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping()
    public ResponseEntity<DeviceResponse> createNewDevice(@RequestBody @Valid DeviceCreateRequest request) {
        log.info("Received device create request: {}", request);
        final DeviceResponse createdDevice = service.createDevice(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }

    @Operation(summary = "Update device")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Device updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Modification failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{uuid}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable UUID uuid, @RequestBody DeviceUpdateRequest request) {
        log.info("Received device {} update request: {}", uuid, request);
        final DeviceResponse updatedDevice = service.updateDevice(uuid, request);

        return ResponseEntity.ok(updatedDevice);
    }
}
