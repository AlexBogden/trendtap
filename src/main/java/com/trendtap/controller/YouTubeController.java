package com.trendtap.controller;

import com.trendtap.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin // allow requests from React later
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/search")
    public String searchYoutube(@RequestParam String q){
        return youTubeService.search(q);
    }

}
