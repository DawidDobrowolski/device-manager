package dd.task.device.manager.interfaces;

import dd.task.device.manager.application.device.DeviceApplicationService;
import dd.task.device.manager.application.device.model.DeviceCreateRequest;
import dd.task.device.manager.application.device.model.DeviceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    content = @Content(mediaType = "text/plain")
            )
    })
    @PostMapping()
    public ResponseEntity<DeviceResponse> createNewDevice(@RequestBody @Valid DeviceCreateRequest request) {
        log.info("Received device create request: {}", request);
        final DeviceResponse createdDevice = service.createDevice(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }
}
