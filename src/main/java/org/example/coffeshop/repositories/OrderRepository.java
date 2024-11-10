package org.example.coffeshop.repositories;

import org.example.coffeshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMenuIdAndStatusIn(Long shopId, List<String> status);
    Order findByUserIdAndStatusIsNot(Long userId, String status);
    Order findByUserId(Long userId);

    @Query(value = """
            SELECT o.id, o.user_id, o.menu_id, o.status, o.queue_id FROM orders o
            INNER JOIN menu m
            ON o.menu_id  = m.id
            INNER JOIN shops s ON
            s.id  = m.shop_id
            WHERE s.id = :shopId
            AND STATUS = :status
            """, nativeQuery = true)
    List<Order> findByShopIdAndStatus(@Param("shopId") Long shopId, @Param("status") String status);

    @Query(value = """
            SELECT o.id, o.user_id, o.menu_id, o.status, o.queue_id FROM orders o
            INNER JOIN menu m
            ON o.menu_id  = m.id
            INNER JOIN shops s ON
            s.id  = m.shop_id
            WHERE s.id = :shopId
            """, nativeQuery = true)
    List<Order> findByShopId(@Param("shopId") Long shopId);

    @Query(value = """
            SELECT o.id, o.user_id, o.menu_id, o.status, o.queue_id FROM orders o
            INNER JOIN menu m
            ON o.menu_id  = m.id
            INNER JOIN shops s ON
            s.id  = m.shop_id
            WHERE s.id = :shopId
            AND o.queue_id IS NULL
            """, nativeQuery = true)
    List<Order> findByShopIdAndQueueIsNull(@Param("shopId") Long shopId);
}
