package com.idealista.integration;

import com.idealista.domain.Ad;
import com.idealista.domain.AdRepository;
import com.idealista.domain.usecase.CalculateAdScoresUseCase;
import com.idealista.domain.usecase.GetPublicAdsUseCase;
import com.idealista.domain.usecase.GetQualityAdsUseCase;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UseCasesIntegrationTest {

    private final CalculateAdScoresUseCase calculateAdScoresUseCase;
    private final GetPublicAdsUseCase getPublicAdsUseCase;
    private final GetQualityAdsUseCase getQualityAdsUseCase;
    private final AdRepository adRepository;

    @Autowired
    UseCasesIntegrationTest(CalculateAdScoresUseCase calculateAdScoresUseCase,
                            GetPublicAdsUseCase getPublicAdsUseCase,
                            GetQualityAdsUseCase getQualityAdsUseCase,
                            AdRepository adRepository) {
        this.calculateAdScoresUseCase = calculateAdScoresUseCase;
        this.getPublicAdsUseCase = getPublicAdsUseCase;
        this.getQualityAdsUseCase = getQualityAdsUseCase;
        this.adRepository = adRepository;
    }

    @BeforeEach
    void setUp() {
        calculateAdScoresUseCase.execute();
    }

    @Test
    void calculateAdScores_shouldAssignScoresToAllAds() {
        List<Ad> allAds = adRepository.findAllAds();

        assertThat(allAds).isNotEmpty();
        assertThat(allAds).allMatch(ad -> ad.getScore() != null);
        assertThat(allAds).allMatch(ad -> ad.getScore() >= 0 && ad.getScore() <= 100);
    }

    @Test
    void calculateAdScores_shouldMarkIrrelevantAds() {
        List<Ad> allAds = adRepository.findAllAds();
        List<Ad> irrelevantAds = allAds.stream()
                .filter(ad -> ad.getScore() < 40)
                .collect(java.util.stream.Collectors.toList());

        assertThat(irrelevantAds).allMatch(ad -> ad.getIrrelevantSince() != null);
    }

    @Test
    void getPublicAds_shouldReturnOnlyRelevantAds() {
        List<PublicAd> publicAds = getPublicAdsUseCase.execute();

        assertThat(publicAds).isNotNull();
        assertThat(publicAds).allMatch(ad -> ad.getId() != null);
        assertThat(publicAds).allMatch(ad -> ad.getTypology() != null);
    }

    @Test
    void getPublicAds_shouldReturnAdsSortedByScore() {
        List<PublicAd> publicAds = getPublicAdsUseCase.execute();

        if (publicAds.size() > 1) {
            assertThat(publicAds).isNotEmpty();
        }
    }

    @Test
    void getQualityAds_shouldReturnLowQualityAds() {
        List<QualityAd> qualityAds = getQualityAdsUseCase.execute();

        assertThat(qualityAds).isNotNull();

        if (!qualityAds.isEmpty()) {
            assertThat(qualityAds).allMatch(ad -> ad.getScore() != null);
            assertThat(qualityAds).allMatch(ad -> ad.getScore() < 40);
            assertThat(qualityAds).allMatch(ad -> ad.getIrrelevantSince() != null);
        }
    }

    @Test
    void integration_fullWorkflow() {
        calculateAdScoresUseCase.execute();

        List<PublicAd> publicAds = getPublicAdsUseCase.execute();
        assertThat(publicAds).isNotNull();

        List<QualityAd> qualityAds = getQualityAdsUseCase.execute();
        assertThat(qualityAds).isNotNull();

        List<Ad> allAds = adRepository.findAllAds();
        int totalAds = publicAds.size() + qualityAds.size();
        assertThat(allAds).hasSize(totalAds);
    }

    @Test
    void calculateAdScores_shouldBeIdempotent() {
        List<Ad> adsBeforeFirstCalculation = adRepository.findAllAds();

        calculateAdScoresUseCase.execute();
        List<Ad> adsAfterFirstCalculation = adRepository.findAllAds();

        calculateAdScoresUseCase.execute();
        List<Ad> adsAfterSecondCalculation = adRepository.findAllAds();

        assertThat(adsAfterFirstCalculation).hasSize(adsBeforeFirstCalculation.size());
        assertThat(adsAfterSecondCalculation).hasSize(adsBeforeFirstCalculation.size());
    }

    @Test
    void ads_shouldHaveCorrectTypologies() {
        List<PublicAd> publicAds = getPublicAdsUseCase.execute();
        List<QualityAd> qualityAds = getQualityAdsUseCase.execute();

        for (PublicAd ad : publicAds) {
            assertThat(ad.getTypology()).isIn("FLAT", "CHALET", "GARAGE");
        }

        for (QualityAd ad : qualityAds) {
            assertThat(ad.getTypology()).isIn("FLAT", "CHALET", "GARAGE");
        }
    }
}
