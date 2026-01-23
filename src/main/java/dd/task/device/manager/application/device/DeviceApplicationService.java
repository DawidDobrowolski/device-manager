package dd.task.device.manager.application.device;

import dd.task.device.manager.application.device.model.DeviceCreateRequest;
import dd.task.device.manager.application.device.model.DeviceResponse;
import dd.task.device.manager.domain.device.DeviceDomainService;
import dd.task.device.manager.domain.device.model.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
