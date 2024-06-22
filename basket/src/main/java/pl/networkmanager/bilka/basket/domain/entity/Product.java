package pl.networkmanager.bilka.basket.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Product {
    private long id;
    private String uuid;
    private String name;
    private String mainDesc;
    private String descHtml;
    private String[] imageUrls;
    private boolean available;
    private LocalDate createdAt;
    private Category category;
}
