package pl.networkmanager.bilka.product.domen.productcrud;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ProductBaseEntity {
    private final String uid = UUID.randomUUID().toString();
    private final LocalDate createdAt = LocalDate.now();
}
