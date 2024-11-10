package org.example.coffeshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.dto.AddMenuRequest;
import org.example.coffeshop.dto.GetMenusResponse;
import org.example.coffeshop.dto.UpdateMenuRequest;
import org.example.coffeshop.services.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    @RequestMapping("/{shopId}")
    public ResponseEntity<List<GetMenusResponse>> getMenuOfShop(@PathVariable long shopId) {
        return ResponseEntity.ok(menuService.getMenuByShopId(shopId));
    }

    @PutMapping("{menuId}")
    public ResponseEntity<String> editMenu(@PathVariable long menuId, @RequestBody UpdateMenuRequest menu) {
        return ResponseEntity.ok(menuService.editMenu(menuId, menu));
    }

    @PostMapping("/{shopId}")
    public ResponseEntity<String> addMenu(@PathVariable long shopId, @RequestBody AddMenuRequest menu) {
        return ResponseEntity.ok(menuService.addMenu(shopId, menu));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable long menuId) {
        return ResponseEntity.ok(menuService.deleteMenu(menuId));
    }
}
