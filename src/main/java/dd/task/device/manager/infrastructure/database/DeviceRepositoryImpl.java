package dd.task.device.manager.infrastructure.database;

import dd.task.device.manager.domain.device.DeviceRepository;
import dd.task.device.manager.domain.device.model.Device;
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
    public List<Device> findByBrandAndName(final String brand, final String name) {
        return jpaRepository.findAll(
                Specification.where(DeviceSpecifications.hasBrand(brand))
                        .and(DeviceSpecifications.hasName(name))
        );
    }

    @Override
    public void delete(final Device device){
        jpaRepository.delete(device);
    }
}
