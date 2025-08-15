package com.trendtap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendtap.dto.YouTubeVideoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class YouTubeService {

    // Inject the API key from application.properties or your .env file
    @Value("${youtube.api.key}")
    private String apiKey;

    // Inject the API host name from application.properties or your .env file
    @Value("${youtube.api.host}")
    private String apiHost;

    /**
     * This method takes a search query, sends a request to the YouTube API, and returns a list of simplified video objects (DTOs).
     */
    public List<YouTubeVideoDTO> search(String query) throws JsonProcessingException {

        System.out.println("SERVICE called with query = " + query);

        // 1. Build the base URL and append query parameters like ?q=mrbeast&hl=en&gl=US
        String url = "https://youtube138.p.rapidapi.com/search/";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("q", query)     // search keyword
                .queryParam("hl", "en")     // language
                .queryParam("gl", "US");    // region

        // 2. Set HTTP headers for authentication (API key and host)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);

        // 3. Wrap the headers in an HttpEntity so we can send it with the request
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 4. Make the GET request using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),  // full URL with query params
                HttpMethod.GET,         // request method
                entity,                 // headers
                String.class            // expect a JSON string
        );

        // 5. Parse the raw JSON response using Jackson's ObjectMapper
        String json = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode contents = mapper.readTree(json).path("contents");

        //Guards the array before looping (avoids surprises if the API shape changes)
        if (!contents.isArray()) return List.of();

        // 6. Create a list to hold our simplified DTO objects
        List<YouTubeVideoDTO> videoList = new ArrayList<>();

        // 7. Loop through each video item in the contents array
        for (JsonNode item : contents) {
            if (!"video".equals(item.path("type").asText(""))) continue;
            JsonNode videoNode = item.path("video");
            System.out.println("TITLE NODE: " + videoNode.path("title").asText(""));


            // Make sure we skip anything that isn't actually a video
            if (!videoNode.isMissingNode()) {
                // Extract videoId safely (or empty string if not found)
                String videoId = videoNode.path("videoId").asText("");

                // Extract video title (prefer direct string; fallback to runs[0].text if needed)
                String title = videoNode.path("title").asText("");
                if (title.isBlank()) {
                    JsonNode runsNode = videoNode.path("title").path("runs");
                    if (runsNode.isArray() && runsNode.size() > 0) {
                        title = runsNode.get(0).path("text").asText("");
                    }
                }

                // Extract thumbnail URL from "thumbnails[0].url"
                String thumbnailUrl = "";
                JsonNode thumbnailsNode = videoNode.path("thumbnails");
                if (thumbnailsNode.isArray() && thumbnailsNode.size() > 0) {
                    thumbnailUrl = thumbnailsNode.get(0).path("url").asText("");
                }

                // Extract channel title from "author.title"
                String channelTitle = videoNode.path("author").path("title").asText("");

                // Extract view count (default to 0 if missing)
                long viewCount = videoNode.path("stats").path("views").asLong(0);

                System.out.println("### COMPUTED TITLE: [" + title + "]");


                // 8. Create a DTO object to represent the video
                YouTubeVideoDTO dto = new YouTubeVideoDTO(
                        videoId,
                        title,
                        thumbnailUrl,
                        channelTitle,
                        viewCount
                );

                // 9. Add it to our list
                videoList.add(dto);
            }
        }

        // 10. Return the full list of simplified video results
        return videoList;
    }
}


