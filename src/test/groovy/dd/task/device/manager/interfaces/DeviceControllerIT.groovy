package dd.task.device.manager.interfaces

import dd.task.device.manager.AbstractIT
import dd.task.device.manager.application.device.model.DeviceCreateRequest
import dd.task.device.manager.application.device.model.DeviceResponse
import dd.task.device.manager.application.device.model.DeviceUpdateRequest
import dd.task.device.manager.domain.device.model.Device
import dd.task.device.manager.domain.device.model.State
import dd.task.device.manager.infrastructure.database.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import java.time.LocalDateTime

class DeviceControllerIT extends AbstractIT {

    @Autowired
    private DeviceController controller

    @Autowired
    private DeviceRepository repository

    void cleanup() {
        repository.deleteAll()
    }

    void 'should create device after request'() {
        given:
        String deviceName = 'deviceName'
        String deviceBrand = 'deviceBrand'
        DeviceCreateRequest createRequest = new DeviceCreateRequest(deviceName, deviceBrand)

        and: 'no device in db'
        repository.findAll().size() == 0

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.postForEntity(
                "http://localhost:$port/api/device-management/devices",
                createRequest,
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.CREATED
        DeviceResponse deviceResponse = response.body

        with(deviceResponse) {
            uuid() != null
            name() == deviceName
            brand() == deviceBrand
            state() == State.AVAILABLE
            creationTime().isAfter(LocalDateTime.now().minusMinutes(1))
        }

        and: 'saved in db'
        repository.findAll().size() == 1
    }

    void 'should not create device when not valid request'() {
        given:
        String deviceName = ''
        String deviceBrand = 'deviceBrand'
        DeviceCreateRequest createRequest = new DeviceCreateRequest(deviceName, deviceBrand)

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.postForEntity(
                "http://localhost:$port/api/device-management/devices",
                createRequest,
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    void 'should update device'() {
        given:
        String newName = 'newName'
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, null)

        and: 'saved device to update'
        Device deviceToUpdate = new Device('name', 'brand')
        repository.save(deviceToUpdate)
        repository.findAll().size() == 1

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${deviceToUpdate.uuid}",
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, null),
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.OK
        DeviceResponse deviceResponse = response.body

        with(deviceResponse) {
            uuid() == deviceToUpdate.uuid
            name() == newName
            brand() == deviceToUpdate.brand
            state() == deviceToUpdate.state
            creationTime().withNano(0) == deviceToUpdate.creationTime.withNano(0)
        }

        and: 'saved in db'
        List<Device> savedDevices = repository.findAll()
        savedDevices.size() == 1
        with(savedDevices[0]) {
            uuid == deviceToUpdate.uuid
            name == newName
            brand == deviceToUpdate.brand
            state == deviceToUpdate.state
            creationTime.withNano(0) == deviceToUpdate.creationTime.withNano(0)
        }
    }


    void 'should not update device when not valid request'() {
        given:
        UUID randomUuid = UUID.randomUUID()
        String newName = ''
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, null)

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${randomUuid}",
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, null),
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    void 'should not update device when device not exist'() {
        given:
        UUID randomUuid = UUID.randomUUID()
        String newName = 'newName'
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, null)

        and: 'no device in db'
        repository.findAll().size() == 0

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${randomUuid}",
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, null),
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.CONFLICT
    }

    void 'should not update device when state IN_USE'() {
        given:
        String newName = 'newName'
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest(newName, null)

        and: 'saved device to update'
        Device deviceToUpdate = new Device('name', 'brand')
        deviceToUpdate.state = State.IN_USE
        repository.save(deviceToUpdate)
        repository.findAll().size() == 1

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${deviceToUpdate.uuid}",
                HttpMethod.PATCH,
                new HttpEntity<>(updateRequest, null),
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.CONFLICT
    }
}
