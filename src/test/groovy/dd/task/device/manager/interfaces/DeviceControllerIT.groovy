package dd.task.device.manager.interfaces

import dd.task.device.manager.AbstractIT
import dd.task.device.manager.application.device.model.DeviceCreateRequest
import dd.task.device.manager.application.device.model.DeviceResponse
import dd.task.device.manager.domain.device.model.State
import dd.task.device.manager.infrastructure.database.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
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
}
