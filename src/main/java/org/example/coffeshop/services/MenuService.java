package org.example.coffeshop.services;

import org.example.coffeshop.dto.AddMenuRequest;
import org.example.coffeshop.dto.GetMenusResponse;
import org.example.coffeshop.dto.UpdateMenuRequest;
import org.example.coffeshop.entities.Menu;

import java.util.List;

public interface MenuService {
    List<GetMenusResponse> getMenuByShopId(Long shopId);
    String addMenu(Long shopId, AddMenuRequest menu);
    String editMenu(Long menuId, UpdateMenuRequest menu);
    String deleteMenu(Long menuId);
}
