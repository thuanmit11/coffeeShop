package org.example.coffeshop.dto;

import lombok.Data;

@Data
public class GetOrdersResponse {

    private String status;
    private String itemName;
    private double price;
    private String customerName;
}
