package org.example.coffeshop.services.impl;

import org.example.coffeshop.services.GeoCodingService;
import org.example.coffeshop.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

    @Value("${opencage-api-key}")
    private String apiKey;


    public Map<String, Double> getCoordinates(String address){
        String BASE_URL = "https://api.opencagedata.com/geocode/v1/geojson";
        String url = BASE_URL + "?q=" + URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8) + "&key=" + apiKey;

        // Create the HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the request and get the response
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JsonUtils.extractCoordinates(response.body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Map.of();
    }
}
