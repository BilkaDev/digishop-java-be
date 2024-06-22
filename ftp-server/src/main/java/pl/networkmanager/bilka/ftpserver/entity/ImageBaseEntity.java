package pl.networkmanager.bilka.ftpserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class ImageBaseEntity implements Serializable {
    private final String uuid = UUID.randomUUID().toString();
    @Column(name = "createdat")
    private final LocalDate createdAt = LocalDate.now();
    @Column(name = "isused")
    private boolean isUsed = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageBaseEntity that = (ImageBaseEntity) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
