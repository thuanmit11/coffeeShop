package org.example.coffeshop.dto;

import lombok.Data;

@Data
public class GetMenusResponse {

    private String description;
    private String itemName;
    private double price;
}
