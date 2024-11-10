package org.example.coffeshop.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {

    public static Map<String, Double> extractCoordinates(String json) {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON string into a JsonNode
            JsonNode rootNode = objectMapper.readTree(json);

            // Access the features array and get the first element
            JsonNode featuresNode = rootNode.path("features");
            if (featuresNode.isArray() && featuresNode.size() > 0) {
                JsonNode firstFeature = featuresNode.get(0);

                // Extract the geometry from the first feature
                JsonNode geometryNode = firstFeature.path("geometry");

                // Extract coordinates and type from the geometry
                JsonNode coordinatesNode = geometryNode.path("coordinates");
                double lng = coordinatesNode.get(0).asDouble(); // Longitude
                double lat = coordinatesNode.get(1).asDouble(); // Latitude
                return Map.of("longitude", lng, "latitude", lat);
                // Print the extracted values
            } else {
                System.out.println("No features found in the response.");
                return Map.of();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
