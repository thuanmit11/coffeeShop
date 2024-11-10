package org.example.coffeshop.dto;

import lombok.Data;
import org.example.coffeshop.entities.Shop;

import java.time.LocalTime;

@Data
public class GetShopsResponse {

    private String name;
    private String location;
    private String contactDetails;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer maxQueueSize;
    private Integer numberOfQueues;

    public GetShopsResponse(Shop shop) {
        this.name = shop.getName();
        this.location = shop.getLocation();
        this.contactDetails = shop.getContactDetails();
        this.closingTime = shop.getClosingTime();
        this.openingTime = shop.getOpeningTime();
        this.maxQueueSize = shop.getMaxQueueSize();
        this.numberOfQueues = shop.getNumberOfQueues();
    }
}
