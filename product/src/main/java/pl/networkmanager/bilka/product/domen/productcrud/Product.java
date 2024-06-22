package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;

import java.math.BigDecimal;

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
    @Column(name = "product_name")
    private String name;
    private boolean activate;
    private String mainDesc;
    private String descHtml;
    private BigDecimal price;


    public Product(long id, String uid, boolean activate, String name, String mainDesc, String descHtml, BigDecimal price, String[] imageUrls, Category category) {
        super(imageUrls);
        this.id = id;
        this.category = category;
        this.name = name;
        this.activate = activate;
        this.mainDesc = mainDesc;
        this.descHtml = descHtml;
        this.price = price;
    }

}
