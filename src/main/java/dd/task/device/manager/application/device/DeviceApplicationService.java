package dd.task.device.manager.application.device;

import dd.task.device.manager.application.device.model.DeviceCreateRequest;
import dd.task.device.manager.application.device.model.DeviceResponse;
import dd.task.device.manager.application.device.model.DeviceUpdateRequest;
import dd.task.device.manager.domain.device.DeviceDomainService;
import dd.task.device.manager.domain.device.model.Device;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceApplicationService {

    private final DeviceDomainService service;
    private final DeviceMapper mapper;

    @Transactional
    public DeviceResponse createDevice(final DeviceCreateRequest createRequest) {
        final Device newDevice = service.createDevice(createRequest.name(), createRequest.brand());

        return mapper.toResponse(newDevice);
    }

    @Transactional
    public DeviceResponse updateDevice(final UUID uuid, final DeviceUpdateRequest updateRequest) {
        validateRequest(updateRequest);
        final Device updatedDevice = service.updateDevice(uuid, updateRequest.name(), updateRequest.brand());

        return mapper.toResponse(updatedDevice);
    }

    private void validateRequest(final DeviceUpdateRequest updateRequest) {
        if (StringUtils.isAllBlank(updateRequest.name(), updateRequest.brand())) {
            throw new IllegalArgumentException("At least one field must be provided");
        }
    }
}
