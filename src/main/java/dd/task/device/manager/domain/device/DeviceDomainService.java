package dd.task.device.manager.domain.device;

import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.infrastructure.common.DeviceModificationException;
import dd.task.device.manager.infrastructure.database.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Device updateDevice(final UUID uuid, final String name, final String brand) {
        final Optional<Device> deviceCandidate = repository.findDeviceByUuid(uuid);
        if (deviceCandidate.isEmpty()) {
            throw new DeviceModificationException("Cannot find device with uuid: %s".formatted(uuid));
        }

        final Device deviceToUpdate = deviceCandidate.get();
        deviceToUpdate.update(name, brand);

        return repository.save(deviceToUpdate);
    }
}
