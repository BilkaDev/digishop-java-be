package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;

import java.math.BigDecimal;
import java.util.Set;

@Table(name = "products")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Product extends ProductBaseEntity {
    @Id
    @GeneratedValue(generator = "products_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "products_id_seq", sequenceName = "products_id_seq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "category_parameters")
    private Category category;
    private String name;
    private boolean activate;
    private String mainDesc;
    private String descHtml;
    private BigDecimal price;
    @ElementCollection
    private Set<String> imageUrls;
}
