package pl.networkmanager.bilka.product.domen.productcrud;

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
public class ProductBaseEntity implements Serializable {
    private final String uuid = UUID.randomUUID().toString();
    private final LocalDate createdAt = LocalDate.now();
    private String[] imageUrls;

    public ProductBaseEntity(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

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
