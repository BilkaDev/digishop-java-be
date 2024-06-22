package pl.networkmanager.bilka.product.domen.categorycrud;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import pl.networkmanager.bilka.product.domen.common.utils.ShortId;

import java.io.Serializable;
import java.util.Objects;

@Getter
@MappedSuperclass
class BaseEntity implements Serializable {
    @Column(name = "short_id")
    private String shortId = ShortId.generate();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(shortId, that.shortId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shortId);
    }
}
