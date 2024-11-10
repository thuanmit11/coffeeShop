package org.example.coffeshop.repositories;

import org.example.coffeshop.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByLocation(String location);

    @Query(value = """
        SELECT * FROM shops s
        WHERE earth_box(ll_to_earth(:latitude, :longitude), :radius) @> ll_to_earth(s.latitude, s.longitude)
        """, nativeQuery = true)
    List<Shop> findShopsNearLocation(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius);  // Radius in meters
}
