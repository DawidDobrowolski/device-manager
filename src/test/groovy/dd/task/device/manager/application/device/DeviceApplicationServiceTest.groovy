package dd.task.device.manager.application.device

import dd.task.device.manager.AbstractTest
import dd.task.device.manager.application.device.model.DeviceUpdateRequest
import dd.task.device.manager.domain.device.DeviceDomainService
import spock.lang.Unroll

class DeviceApplicationServiceTest extends AbstractTest {

    private DeviceDomainService domainService = Mock()
    private DeviceMapper mapper = new DeviceMapperImpl()

    private DeviceApplicationService service = new DeviceApplicationService(domainService, mapper)

    void 'should not trow exception when valid update request'() {
        given:
        UUID uuid = UUID.randomUUID()
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, newBrand)

        when:
        service.updateDevice(uuid, updateRequest)

        then:
        noExceptionThrown()

        where:
        newName   | newBrand
        ''        | 'newBrand'
        null      | 'newBrand'
        'mewName' | ''
        'mewName' | null
    }

    @Unroll
    void 'should trow exception when invalid update request'() {
        given:
        UUID uuid = UUID.randomUUID()
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, newBrand)

        when:
        service.updateDevice(uuid, updateRequest)

        then:
        IllegalArgumentException e = thrown(IllegalArgumentException)
        e.message == "At least one field must be provided"

        where:
        newName | newBrand
        ''      | ''
        ''      | null
        null    | ''
        null    | null
    }
}
