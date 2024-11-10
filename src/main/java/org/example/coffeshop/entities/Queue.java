package org.example.coffeshop.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = Queue.TABLE_NAME)
public class Queue {
    public static final String TABLE_NAME = "queues";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private int maxSize;
    private int currentSize;

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> customers = new ArrayList<>();

    // One-to-many relationship with Order
    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public void addCustomer(User customer) {
        if (this.currentSize < this.maxSize) {
            this.customers.add(customer);
            this.currentSize++;
            customer.setQueue(this);
        }
    }

    public void serveCustomer(User customer) {
        if (this.customers.remove(customer)) {
            this.currentSize--;
            customer.setQueue(null);
        }
    }
}
