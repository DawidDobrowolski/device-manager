package dd.task.device.manager.domain.device;

import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.domain.device.model.State;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findByUuid(UUID uuid);

    List<Device> findByStateAndBrand(State state, String brand);

    void delete(Device device);
}
