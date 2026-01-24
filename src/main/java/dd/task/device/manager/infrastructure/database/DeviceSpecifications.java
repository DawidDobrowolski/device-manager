package dd.task.device.manager.infrastructure.database;

import dd.task.device.manager.domain.device.model.Device;
import dd.task.device.manager.domain.device.model.Device_;
import dd.task.device.manager.domain.device.model.State;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class DeviceSpecifications {

    public static Specification<Device> hasBrand(String brand) {
        final String brandNormalized = StringUtils.lowerCase(StringUtils.normalizeSpace(brand));

        return (root, query, cb) ->
                StringUtils.isBlank(brand) ? null : cb.equal(root.get(Device_.BRAND_NORMALIZED), brandNormalized);
    }

    public static Specification<Device> hasState(State state) {
        return (root, query, cb) ->
                state == null ? null : cb.equal(root.get(Device_.STATE), state);
    }
}
