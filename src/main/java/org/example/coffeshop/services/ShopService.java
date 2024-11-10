package org.example.coffeshop.services;

import org.example.coffeshop.dto.GetQueueResponse;
import org.example.coffeshop.dto.UpdateShopRequest;
import org.example.coffeshop.entities.Shop;

import java.util.List;
import java.util.Optional;

public interface ShopService {

    List<Shop> getAllShops();
    Optional<Shop> getShopById(Long id);
    String createShop(Shop shop);
    String updateShop(Long id, UpdateShopRequest shopDetails);
    boolean deleteShop(Long id);
    Shop getShopNearBy(String username);
    GetQueueResponse getQueuesByShopId(Long shopId);
}
