package dd.task.device.manager.infrastructure.database;

import dd.task.device.manager.domain.device.DeviceRepository;
import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.domain.device.model.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DeviceJpaRepository jpaRepository;

    @Override
    public Device save(final Device device) {
        return jpaRepository.save(device);
    }

    @Override
    public Optional<Device> findByUuid(final UUID uuid) {
        return jpaRepository.findDeviceByUuid(uuid);
    }

    @Override
    public List<Device> findByStateAndBrand(final State state, final String brand) {
        return jpaRepository.findAll(
                Specification.where(DeviceSpecifications.hasBrand(brand))
                        .and(DeviceSpecifications.hasState(state))
        );
    }

    @Override
    public void delete(final Device device) {
        jpaRepository.delete(device);
    }
}
