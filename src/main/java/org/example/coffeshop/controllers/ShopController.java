package org.example.coffeshop.controllers;

import org.example.coffeshop.dto.GetQueueResponse;
import org.example.coffeshop.dto.GetShopsResponse;
import org.example.coffeshop.dto.UpdateShopRequest;
import org.example.coffeshop.entities.Shop;
import org.example.coffeshop.services.JWTService;
import org.example.coffeshop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopService shopService;
    private final JWTService jwtService;

    @Autowired
    public ShopController(ShopService shopService,
                          JWTService jwtService) {
        this.shopService = shopService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<GetShopsResponse>> getAllShops() {
        List<GetShopsResponse> shops = shopService.getAllShops();
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetShopsResponse> getShopById(@PathVariable Long id) {
        return new ResponseEntity<>(shopService.getShopById(id), HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createShop(@Validated @RequestBody Shop shop) {
        return new ResponseEntity<>(shopService.createShop(shop), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateShop(@PathVariable Long id, @Validated @RequestBody UpdateShopRequest shopDetails) {
        return new ResponseEntity<>(shopService.updateShop(id, shopDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        boolean isDeleted = shopService.deleteShop(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/nearest")
    public ResponseEntity<GetShopsResponse> getClosetShop(@RequestHeader("Authorization") String authorizationHeader) {
        String username = jwtService.extractUserName(authorizationHeader.substring(7));
        return new ResponseEntity<>(shopService.getShopNearBy(username), HttpStatus.OK);
    }

    @GetMapping("{shopId}/queue")
    public ResponseEntity<GetQueueResponse> getQueues(@PathVariable("shopId") Long shopId) {
        return new ResponseEntity<>(shopService.getQueuesByShopId(shopId), HttpStatus.OK);
    }
}