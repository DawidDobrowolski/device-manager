package dd.task.device.manager.application.device;

import dd.task.device.manager.application.device.model.DeviceResponse;
import dd.task.device.manager.domain.device.model.Device;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceMapper {

    DeviceResponse toResponse(Device domain);
}
