package org.example.coffeshop.dto;

import lombok.Data;

@Data
public class AddMenuRequest {

    private String itemName;
    private double price;
    private String description;
}
