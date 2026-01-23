package dd.task.device.manager.application.device

import dd.task.device.manager.AbstractTest
import dd.task.device.manager.application.device.model.DeviceResponse
import dd.task.device.manager.domain.device.model.Device

class DeviceMapperTest extends AbstractTest {

    DeviceMapper mapper = new DeviceMapperImpl()

    void 'should map domain to response'() {
        given:
        Device domain = new Device('deviceName', 'deviceBrand')

        when:
        DeviceResponse response = mapper.toResponse(domain)

        then:
        with(response) {
            uuid() == domain.uuid
            name() == domain.name
            brand() == domain.brand
            state() == domain.state
            creationTime() == domain.creationTime
        }
    }
}
