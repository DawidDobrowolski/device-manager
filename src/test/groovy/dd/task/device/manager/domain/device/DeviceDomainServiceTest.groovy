package dd.task.device.manager.domain.device

import dd.task.device.manager.AbstractTest
import dd.task.device.manager.infrastructure.common.DeviceModificationException
import dd.task.device.manager.infrastructure.database.DeviceRepository

class DeviceDomainServiceTest extends AbstractTest {

    private  DeviceRepository repository = Mock()

    private DeviceDomainService service = new DeviceDomainService(repository)

    void 'should throw exception when device to update not found'(){
        given:
        UUID uuid = UUID.randomUUID()

        when:
        service.updateDevice(uuid, null, null)

        then:
        1 * repository.findDeviceByUuid(uuid) >> Optional.empty()

        DeviceModificationException e = thrown(DeviceModificationException)
        e.message == "Cannot find device with uuid: $uuid"
    }
}
