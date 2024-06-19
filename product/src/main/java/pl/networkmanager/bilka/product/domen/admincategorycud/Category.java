package pl.networkmanager.bilka.product.domen.admincategorycud;

import jakarta.persistence.*;
import lombok.Builder;

@Table(name = "category_parameters")
@Entity
@Builder
record Category(
        @Id
        @GeneratedValue(generator = "category_parameters_id_seq", strategy = GenerationType.SEQUENCE)
        @SequenceGenerator(name = "category_parameters_id_seq", sequenceName = "category_parameters_id_seq", allocationSize = 1)
        Long id,
        String shortId,
        @Column(name = "category_name")
        String name
) {
}
