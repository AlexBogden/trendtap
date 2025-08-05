package com.trendtap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendtap.dto.YouTubeVideoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.host}")
    private String apiHost;

    public List<YouTubeVideoDTO> search(String query) throws JsonProcessingException {
        // Base URL for the API endpoint
        String url = "https://youtube138.p.rapidapi.com/search/";

        // Build the full URL with query parameters (like ?q=mrbeast&hl=en&gl=US)
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("q", query)
                .queryParam("hl", "en") // Language
                .queryParam("gl", "US"); // Region

        // Create headers for authentication
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);

        // Bundle the headers into a request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Create a RestTemplate to send the GET request
        RestTemplate restTemplate = new RestTemplate();

        // Send the request, get the response
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), // the full URL with query params
                HttpMethod.GET,        // GET request
                entity,                // request headers
                String.class           // expect a String (raw JSON)
        );

        // Store the raw JSON response as a String for parsing
        String json =  response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode contents = root.path("contents");

        List<YouTubeVideoDTO> videoList = new ArrayList<>();

        for (JsonNode item : contents) {
            JsonNode videoNode = item.path("video");

            if (!videoNode.isMissingNode()) {
                String videoId = videoNode.path("videoId").asText();
                String title = videoNode.path("title").path("runs").get(0).path("text").asText();
                String thumbnailUrl = videoNode.path("thumbnails").get(0).path("url").asText();
                String channelTitle = videoNode.path("author").path("title").asText();
                long viewCount = videoNode.path("stats").path("views").asLong();

                YouTubeVideoDTO dto = new YouTubeVideoDTO(
                        videoId,
                        title,
                        thumbnailUrl,
                        channelTitle,
                        viewCount
                );

                videoList.add(dto);
            }
        }
        return videoList;
    }
}

