package org.example.coffeshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.coffeshop.dto.AddMenuRequest;
import org.example.coffeshop.dto.GetMenusResponse;
import org.example.coffeshop.dto.UpdateMenuRequest;
import org.example.coffeshop.entities.Menu;
import org.example.coffeshop.entities.Order;
import org.example.coffeshop.entities.Shop;
import org.example.coffeshop.repositories.MenuRepository;
import org.example.coffeshop.repositories.OrderRepository;
import org.example.coffeshop.repositories.ShopRepository;
import org.example.coffeshop.services.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<GetMenusResponse> getMenuByShopId(Long shopId) {
        List<Menu> menus = menuRepository.findByShopId(shopId);
        if (menus.isEmpty()) {
            return List.of();
        }

        return menus.stream().map(menu -> {
            GetMenusResponse response = new GetMenusResponse();
            response.setDescription(menu.getDescription());
            response.setItemName(menu.getItemName());
            response.setPrice(menu.getPrice());
            return response;
        }).toList();
    }

    @Override
    public String addMenu(Long shopId, AddMenuRequest menu) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            Menu menuEntity = new Menu();
            menuEntity.setShop(shop.get());
            menuEntity.setPrice(menu.getPrice());
            menuEntity.setItemName(menu.getItemName());
            menuEntity.setDescription(menu.getDescription());
            menuRepository.save(menuEntity);
        }
        return "Menu added successfully";
    }

    @Override
    public String editMenu(Long menuId, UpdateMenuRequest menu) {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isPresent()) {
            Menu currentMenu = optionalMenu.get();
            currentMenu.setItemName(menu.getItemName());
            currentMenu.setPrice(menu.getPrice());
            currentMenu.setDescription(menu.getDescription());
            menuRepository.save(currentMenu);
            return "Menu edited successfully";
        }
        return "Menu not found";
    }

    @Override
    public String deleteMenu(Long menuId) {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isPresent()) {
            menuRepository.deleteById(menuId);
            return "Menu deleted successfully";
        }
        return "Menu not found";
    }
}
