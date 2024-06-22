package pl.networkmanager.bilka.basket.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Basket extends BaseEntity {
    @Id
    @GeneratedValue(generator = "basket_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "basket_id_seq", sequenceName = "basket_id_seq", allocationSize = 1)
    private Long id;
}
