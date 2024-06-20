package pl.networkmanager.bilka.product.domen.categorycrud;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "category_parameters")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(generator = "category_parameters_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_parameters_id_seq", sequenceName = "category_parameters_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "category_name")
    private String name;
}