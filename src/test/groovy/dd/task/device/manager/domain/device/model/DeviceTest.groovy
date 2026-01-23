package dd.task.device.manager.domain.device.model

import dd.task.device.manager.AbstractTest

import java.time.LocalDateTime

class DeviceTest extends AbstractTest {

    void 'should create new device'() {
        given:
        String testName = 'testName'
        String testBrand = ' test   Brand  '

        when:
        Device createdDevice = new Device(testName, testBrand)

        then:
        with(createdDevice) {
            uuid != null
            name == testName
            brand == testBrand
            brandNormalized == 'test brand'
            state == State.AVAILABLE
            creationTime.isAfter(LocalDateTime.now().minusMinutes(1))
        }
    }
}
