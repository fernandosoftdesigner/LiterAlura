package com.example.bookcatalog.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GutendexService {
    private static final String API_URL = "https://gutendex.com/books?search=";

    public JsonObject searchBook(String title) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL + title, String.class);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray results = jsonResponse.getAsJsonArray("results");

        if (results.size() > 0) {
            return results.get(0).getAsJsonObject();
        } else {
            return null;
        }
    }
}
