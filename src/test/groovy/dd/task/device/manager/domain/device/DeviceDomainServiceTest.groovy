package dd.task.device.manager.domain.device

import dd.task.device.manager.AbstractTest
import dd.task.device.manager.domain.device.model.Device
import dd.task.device.manager.domain.device.model.State
import dd.task.device.manager.infrastructure.common.DeviceModificationException
import dd.task.device.manager.infrastructure.common.DeviceNotFoundException

class DeviceDomainServiceTest extends AbstractTest {

    private DeviceRepository repository = Mock()

    private DeviceDomainService service = new DeviceDomainService(repository)

    void 'should throw exception when device  not found'() {
        given:
        UUID uuid = UUID.randomUUID()

        when:
        service.getDevice(uuid)

        then:
        1 * repository.findByUuid(uuid) >> Optional.empty()

        DeviceNotFoundException e = thrown(DeviceNotFoundException)
        e.message == "Cannot find device with uuid: $uuid"
    }

    void 'should throw exception when device cannot be deleted'() {
        given:
        Device deviceToUpdate = new Device('name', 'brand')
        deviceToUpdate.state = State.IN_USE

        when:
        service.deleteDevice(deviceToUpdate.uuid)
        deviceToUpdate.update('newName', 'newBrand')

        then:
        1 * repository.findByUuid(deviceToUpdate.uuid) >> Optional.of(deviceToUpdate)

        DeviceModificationException e = thrown(DeviceModificationException)
        e.message == "Device ${deviceToUpdate.uuid} cannot be updated"
    }
}
