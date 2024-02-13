package com.example.micrometerboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdsController {

    @GetMapping("/ads/view")
    public Map<String, String> adsView(@RequestParam String id) {
        log.info("[BEG] adsView : {}", id);
        String adsId = "ADS-1000";
        String redirectLink = "https://redirect-url.com";
        String imgUrl = "https://image-url.com";
        Map<String, String> response = new Hashtable<>();
        response.put("ads_id", adsId);
        response.put("redirect_link", redirectLink);
        response.put("img_url", imgUrl);
        log.info("[END] adsView : {}", id);
        return response;
    }

    @GetMapping("/ads/click")
    public Map<String, String> adsClick(@RequestParam String id, @RequestParam String adsId) {
        log.info("[BEG] adsClick : {}, {}", id, adsId);
        Map<String, String> response = new Hashtable<>();
        response.put("result", "ok");
        log.info("[END] adsClick : {}, {}", id, adsId);
        return response;
    }

}
