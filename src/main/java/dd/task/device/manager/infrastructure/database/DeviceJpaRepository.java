package dd.task.device.manager.infrastructure.database;

import dd.task.device.manager.domain.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceJpaRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    Optional<Device> findDeviceByUuid(UUID uuid);
}
