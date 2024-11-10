package org.example.coffeshop.repositories;

import org.example.coffeshop.entities.Menu;
import org.example.coffeshop.entities.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByShopId(Long name);
}
