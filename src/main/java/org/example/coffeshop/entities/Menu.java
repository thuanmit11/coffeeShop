package org.example.coffeshop.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = Menu.TABLE_NAME)
public class Menu {

    public static final String TABLE_NAME = "menu";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    private Double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "menu", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> order;

}
