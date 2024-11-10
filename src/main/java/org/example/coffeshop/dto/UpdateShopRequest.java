package org.example.coffeshop.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateShopRequest {

    private String location;
    private String contactDetails;
    private Integer maxQueueSize;
    private Integer numberOfQueues;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
