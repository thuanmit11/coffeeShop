package org.example.coffeshop.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = Shop.TABLE_NAME)
public class Shop {

    public static final String TABLE_NAME = "shops";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String contactDetails;

    @Column(name = "max_queue_size")
    private Integer maxQueueSize;
    private Double latitude;
    private Double longitude;

    @Column(name = "number_of_queues")
    private Integer numberOfQueues;

    private LocalTime openingTime;
    private LocalTime closingTime;

    @OneToMany(mappedBy = "shop")
    private List<Queue> queues;
}
