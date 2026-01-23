package dd.task.device.manager.domain.device.model

import dd.task.device.manager.AbstractTest
import dd.task.device.manager.infrastructure.common.DeviceModificationException
import spock.lang.Unroll

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

    @Unroll
    void 'should update device'() {
        given:
        Device deviceToUpdate = new Device('name', 'brand')
        deviceToUpdate.state = beforeUpdateState
        UUID uuidBeforeUpdate = deviceToUpdate.uuid
        State stateBeforeUpdate = deviceToUpdate.state
        LocalDateTime timeBeforeUpdate = deviceToUpdate.creationTime

        when:
        deviceToUpdate.update(newName, newBrand)

        then:
        with(deviceToUpdate) {
            uuid == uuidBeforeUpdate
            name == updatedName
            brand == updatedBrand
            state == stateBeforeUpdate
            creationTime == timeBeforeUpdate
        }

        where:
        newName   | newBrand   | beforeUpdateState || updatedName | updatedBrand
        'newName' | 'newBrand' | State.AVAILABLE   || 'newName'   | 'newBrand'
        'newName' | ''         | State.AVAILABLE   || 'newName'   | 'brand'
        null      | 'newBrand' | State.INACTIVE    || 'name'      | 'newBrand'
        ''        | null       | State.INACTIVE    || 'name'      | 'brand'
    }

    void 'should throw exception when device not updatable'() {
        given:
        Device deviceToUpdate = new Device('name', 'brand')
        deviceToUpdate.state = State.IN_USE

        when:
        deviceToUpdate.update('newName', 'newBrand')

        then:
        DeviceModificationException e = thrown(DeviceModificationException)
        e.message == "Device ${deviceToUpdate.uuid} cannot be updated"
    }
}
