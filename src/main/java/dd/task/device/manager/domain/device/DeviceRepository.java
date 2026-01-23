package dd.task.device.manager.domain.device;

import dd.task.device.manager.domain.device.model.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findByUuid(UUID uuid);

    List<Device> findByBrandAndName(String brand, String name);

    void delete(Device device);
}
