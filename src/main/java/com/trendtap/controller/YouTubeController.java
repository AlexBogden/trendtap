package com.trendtap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendtap.dto.YouTubeVideoDTO;
import com.trendtap.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin // allow requests from React later
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/search")
    public List<YouTubeVideoDTO> searchYoutube(@RequestParam String q) throws JsonProcessingException {
        return youTubeService.search(q);
    }
}
