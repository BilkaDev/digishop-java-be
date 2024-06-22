package pl.networkmanager.bilka.basket.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {
    long id;
    String name;
    String shortId;
}
