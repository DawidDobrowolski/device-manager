package dd.task.device.manager.domain.device.model;

import dd.task.device.manager.infrastructure.common.DeviceModificationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@ToString
@Entity
@Table(name = "device")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq_gen")
    @SequenceGenerator(name = "device_seq_gen", sequenceName = "device_seq")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "brand_normalized", nullable = false)
    private String brandNormalized;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    public Device(final String name, final String brand) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.brand = brand;
        this.brandNormalized = StringUtils.lowerCase(StringUtils.normalizeSpace(brand));
        this.state = State.AVAILABLE;
        this.creationTime = LocalDateTime.now();
    }

    public void update(final String newName, final String newBrand) {
        if (notModifiable()) {
            throw new DeviceModificationException("Device %s cannot be updated".formatted(this.uuid));
        }

        updateField(newName, name -> this.name = name);
        updateField(newBrand, brand -> this.brand = brand);
    }

    public boolean notModifiable() {
        return this.state == State.IN_USE;
    }

    private void updateField(final String newValue, final Consumer<String> assign) {
        Optional.ofNullable(newValue).filter(StringUtils::isNotBlank).ifPresent(assign);
    }
}
