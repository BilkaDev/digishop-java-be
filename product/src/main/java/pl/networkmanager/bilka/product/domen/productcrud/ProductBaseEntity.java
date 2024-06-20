package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class ProductBaseEntity implements Serializable {
    private final String uuid = UUID.randomUUID().toString();
    private final LocalDate createdAt = LocalDate.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductBaseEntity that = (ProductBaseEntity) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
