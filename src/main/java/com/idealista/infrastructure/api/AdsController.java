package com.idealista.infrastructure.api;

import java.util.List;

import com.idealista.application.AdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdsController {

    private final AdsService adsService;

    @Autowired
    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping("/ads/quality")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        return ResponseEntity.ok(adsService.findQualityAds());
    }

    @GetMapping("/ads/public")
    public ResponseEntity<List<PublicAd>> publicListing() {
        return ResponseEntity.ok(adsService.findPublicAds());
    }

    @PostMapping("/ads/score")
    public ResponseEntity<Void> calculateScore() {
        adsService.calculateScores();
        return ResponseEntity.accepted().build();
    }
}
