package org.example.coffeshop.services;

import org.example.coffeshop.entities.Queue;
import org.example.coffeshop.entities.Shop;
import org.example.coffeshop.entities.User;

import java.util.List;

public interface QueueService {

    Queue addQueueToShop(Shop shop);
    void removeQueue(Long queueId);
    void updateQueue(Long queueId, Queue queue);
    List<Queue> findByShopId(Long shopId);
    void updateQueues(List<Queue> queues, Shop shop);
}
