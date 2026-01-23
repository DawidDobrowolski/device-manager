package dd.task.device.manager.domain.device;

import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.infrastructure.common.DeviceModificationException;
import dd.task.device.manager.infrastructure.common.DeviceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
        final Device deviceToUpdate = getDevice(uuid);
        deviceToUpdate.update(name, brand);

        return repository.save(deviceToUpdate);
    }

    public Device getDevice(final UUID uuid) {
        final Optional<Device> deviceCandidate = repository.findByUuid(uuid);
        if (deviceCandidate.isEmpty()) {
            throw new DeviceNotFoundException("Cannot find device with uuid: %s".formatted(uuid));
        }

        return deviceCandidate.get();
    }

    public List<Device> findDevices(final String name, final String brand) {
        return repository.findByBrandAndName(name, brand);
    }

    public void deleteDevice(final UUID uuid) {
        final Device deviceToDelete = getDevice(uuid);
        if (deviceToDelete.notModifiable()) {
            throw new DeviceModificationException("Device %s cannot be updated".formatted(uuid));
        }

        repository.delete(deviceToDelete);
    }
}
