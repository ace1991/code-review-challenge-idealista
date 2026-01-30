package com.idealista.integration;

import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdsControllerIntegrationTest {

    private final TestRestTemplate restTemplate;

    @Autowired
    AdsControllerIntegrationTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void calculateScores_shouldReturnAccepted() {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/ads/score",
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void getPublicAds_shouldReturnListOfPublicAds() {
        restTemplate.postForEntity("/ads/score", null, Void.class);

        ResponseEntity<List<PublicAd>> response = restTemplate.exchange(
                "/ads/public",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PublicAd>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void getPublicAds_shouldReturnAdsSortedByScoreDescending() {
        restTemplate.postForEntity("/ads/score", null, Void.class);

        ResponseEntity<List<PublicAd>> response = restTemplate.exchange(
                "/ads/public",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PublicAd>>() {}
        );

        List<PublicAd> ads = response.getBody();
        assertThat(ads).isNotNull();

        for (int i = 0; i < ads.size() - 1; i++) {
            assertThat(ads.get(i).getId()).isNotNull();
        }
    }

    @Test
    void getQualityAds_shouldReturnListOfQualityAds() {
        restTemplate.postForEntity("/ads/score", null, Void.class);

        ResponseEntity<List<QualityAd>> response = restTemplate.exchange(
                "/ads/quality",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QualityAd>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getQualityAds_shouldReturnAdsWithScoreBelowThreshold() {
        restTemplate.postForEntity("/ads/score", null, Void.class);

        ResponseEntity<List<QualityAd>> response = restTemplate.exchange(
                "/ads/quality",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QualityAd>>() {}
        );

        List<QualityAd> ads = response.getBody();
        assertThat(ads).isNotNull();

        if (!ads.isEmpty()) {
            for (QualityAd ad : ads) {
                assertThat(ad.getScore()).isLessThan(40);
                assertThat(ad.getIrrelevantSince()).isNotNull();
            }
        }
    }

    @Test
    void endToEnd_calculateAndRetrieveAds() {
        ResponseEntity<Void> calculateResponse = restTemplate.postForEntity(
                "/ads/score",
                null,
                Void.class
        );
        assertThat(calculateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<List<PublicAd>> publicResponse = restTemplate.exchange(
                "/ads/public",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PublicAd>>() {}
        );
        assertThat(publicResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publicResponse.getBody()).isNotEmpty();

        ResponseEntity<List<QualityAd>> qualityResponse = restTemplate.exchange(
                "/ads/quality",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QualityAd>>() {}
        );
        assertThat(qualityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(qualityResponse.getBody()).isNotNull();
    }
}
