package dd.task.device.manager.interfaces

import dd.task.device.manager.AbstractIT
import dd.task.device.manager.application.device.model.DeviceCreateRequest
import dd.task.device.manager.application.device.model.DeviceResponse
import dd.task.device.manager.application.device.model.DeviceUpdateRequest
import dd.task.device.manager.domain.device.model.Device
import dd.task.device.manager.domain.device.model.State
import dd.task.device.manager.infrastructure.database.DeviceJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import java.time.LocalDateTime

class DeviceControllerIT extends AbstractIT {

    @Autowired
    private DeviceController controller

    @Autowired
    private DeviceJpaRepository repository

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
        response.statusCode == HttpStatus.NOT_FOUND
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

    void 'should fetch device'() {
        given:
        Device deviceToFetch = new Device('name', 'brand')
        repository.save(deviceToFetch)
        repository.findAll().size() == 1

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.getForEntity(
                "http://localhost:$port/api/device-management/devices/${deviceToFetch.uuid}",
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.OK
        DeviceResponse deviceResponse = response.body

        with(deviceResponse) {
            uuid() == deviceToFetch.uuid
            name() == deviceToFetch.name
            brand() == deviceToFetch.brand
            state() == deviceToFetch.state
            creationTime().withNano(0) == deviceToFetch.creationTime.withNano(0)
        }
    }

    void 'should return error when device not exist'() {
        given:
        UUID uuid = UUID.randomUUID()

        and: 'no device in db'
        repository.findAll().size() == 0

        when:
        ResponseEntity<DeviceResponse> response = restTemplate.getForEntity(
                "http://localhost:$port/api/device-management/devices/${uuid}",
                DeviceResponse.class
        )

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    void 'should fetch all devices'() {
        given:
        Device firstDevice = new Device('name1', 'brand1')
        Device secondDevice = new Device('name2', 'brand2')
        repository.saveAll([firstDevice, secondDevice])
        repository.findAll().size() == 2

        when:
        ResponseEntity<List<DeviceResponse>> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceResponse>>() {})

        then:
        response.statusCode == HttpStatus.OK
        List<DeviceResponse> foundDevices = response.body
        foundDevices.size() == 2

        with(foundDevices.find { it.uuid() == firstDevice.uuid }) {
            name() == firstDevice.name
            brand() == firstDevice.brand
        }

        with(foundDevices.find { it.uuid() == secondDevice.uuid }) {
            name() == secondDevice.name
            brand() == secondDevice.brand
        }
    }

    void 'should return empty list when no devices'() {
        given:
        repository.findAll().size() == 0

        when:
        ResponseEntity<List<DeviceResponse>> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceResponse>>() {})

        then:
        response.statusCode == HttpStatus.OK
        List<DeviceResponse> foundDevices = response.body
        foundDevices.size() == 0
    }

    void 'should fetch all devices by state'() {
        given:
        Device firstDevice = new Device('name1', 'brand1')
        firstDevice.state = State.INACTIVE
        Device secondDevice = new Device('name2', 'brand2')
        repository.saveAll([firstDevice, secondDevice])
        repository.findAll().size() == 2

        when:
        ResponseEntity<List<DeviceResponse>> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices?state=${secondDevice.state}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceResponse>>() {})

        then:
        response.statusCode == HttpStatus.OK
        List<DeviceResponse> foundDevices = response.body
        foundDevices.size() == 1

        with(foundDevices[0]) {
            name() == secondDevice.name
            brand() == secondDevice.brand
        }
    }

    void 'should fetch all devices by brand'() {
        given:
        Device firstDevice = new Device('name1', ' brandTest  ')
        Device secondDevice = new Device('name2', 'brand2')
        repository.saveAll([firstDevice, secondDevice])
        repository.findAll().size() == 2

        when:
        ResponseEntity<List<DeviceResponse>> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices?state=&brand=brandTest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceResponse>>() {})

        then:
        response.statusCode == HttpStatus.OK
        List<DeviceResponse> foundDevices = response.body
        foundDevices.size() == 1

        with(foundDevices[0]) {
            name() == firstDevice.name
            brand() == firstDevice.brand
        }
    }

    void 'should delete device'() {
        given:
        Device deviceToUpdate = new Device('name', 'brand')
        repository.save(deviceToUpdate)
        repository.findAll().size() == 1

        when:
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${deviceToUpdate.uuid}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void
        )

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        and: 'empty db'
        repository.findAll().size() == 0
    }

    void 'should not delete when device not exist'() {
        given:
        UUID uuid = UUID.randomUUID()

        and: 'device with other uuid'
        Device deviceToUpdate = new Device('name', 'brand')
        repository.save(deviceToUpdate)
        repository.findAll().size() == 1

        when:
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${uuid}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void
        )

        then:
        response.statusCode == HttpStatus.NOT_FOUND

        and: 'empty db'
        repository.findAll().size() == 1
    }

    void 'should not delete device when state IN_USE'() {
        given:
        Device deviceToUpdate = new Device('name', 'brand')
        deviceToUpdate.state = State.IN_USE
        repository.save(deviceToUpdate)
        repository.findAll().size() == 1

        when:
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:$port/api/device-management/devices/${deviceToUpdate.uuid}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void
        )

        then:
        response.statusCode == HttpStatus.CONFLICT
    }
}
