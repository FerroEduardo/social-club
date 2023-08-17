package com.softawii.social.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

@Component
public class SteamService {
    @Value("${softawii.steam.token}")
    private String steamApiToken;
    private String steamApiUrl = "https://api.steampowered.com";

    public Map<String, Object> getUserData(String steamUserId) throws Exception {
        RestTemplate           restTemplate = new RestTemplate();
        String                 url          = String.format("%s/ISteamUser/GetPlayerSummaries/v2/?key=%s&format=json&steamids=%s", steamApiUrl, steamApiToken, steamUserId);
        ResponseEntity<String> response     = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            throw new Exception(); // TODO: USE A BETTER EXCEPTION
        }

        ObjectMapper        mapper          = new ObjectMapper();
        JsonNode            tree            = mapper.readTree(response.getBody());
        Iterator<JsonNode>  playersIterator = tree.get("response").get("players").iterator();
        Map<String, Object> map             = (Map<String, Object>) mapper.convertValue(playersIterator.next(), Map.class);

        return map;
    }
}
