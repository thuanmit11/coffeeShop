package org.example.coffeshop.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateMenuRequest {

    private String itemName;
    private double price;
    private String description;
}
