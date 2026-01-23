package dd.task.device.manager.domain.device;

import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.infrastructure.database.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceDomainService {

    private final DeviceRepository repository;

    public Device createDevice(final String name, final String brand) {
        final Device newDevice = new Device(name, brand);
        log.info("About to save new device: {}", newDevice);

        return repository.save(newDevice);
    }
}
