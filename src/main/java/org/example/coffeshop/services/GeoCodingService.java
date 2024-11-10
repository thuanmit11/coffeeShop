package org.example.coffeshop.services;

import java.util.Map;

public interface GeoCodingService {

    Map<String, Double> getCoordinates(String address);
}
