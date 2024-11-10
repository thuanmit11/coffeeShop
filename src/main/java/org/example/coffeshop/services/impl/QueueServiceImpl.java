package org.example.coffeshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.entities.Queue;
import org.example.coffeshop.entities.Shop;
import org.example.coffeshop.entities.User;
import org.example.coffeshop.repositories.QueueRepository;
import org.example.coffeshop.repositories.UserRepository;
import org.example.coffeshop.services.QueueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final UserRepository userRepository;

    @Override
    public void removeQueue(Long queueId) {
        Optional<Queue> optionalQueue = queueRepository.findById(queueId);
        optionalQueue.ifPresent(queueRepository::delete);
    }

    @Override
    public void updateQueue(Long queueId, Queue queue) {
        Optional<Queue> optionalQueue = queueRepository.findById(queueId);
        optionalQueue.ifPresent(queueRepository::save);
    }

    @Override
    public List<Queue> findByShopId(Long shopId) {
        return queueRepository.findByShopId(shopId);
    }

    @Override
    public Queue addQueueToShop(Shop shop) {
        Queue queue = new Queue();
        queue.setShop(shop);
        queue.setCurrentSize(0);
        queue.setMaxSize(shop.getMaxQueueSize());
        return queueRepository.save(queue);
    }

    public void updateQueues(List<Queue> queues, Shop shop) {
        queues.forEach(queue -> queue.setMaxSize(shop.getMaxQueueSize()));
        queueRepository.saveAll(queues);
    }
}
