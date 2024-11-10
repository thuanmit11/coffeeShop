package org.example.coffeshop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = Order.TABLE_NAME)
public class Order {
    public static final String TABLE_NAME = "orders";

    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_IN_QUEUE = "IN_QUEUE";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELED = "CANCELED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private String status;

    // Many-to-one relationship with Queue
    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;


}
