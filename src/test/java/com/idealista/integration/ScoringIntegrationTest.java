package com.idealista.integration;

import com.idealista.domain.*;
import com.idealista.domain.scoring.AdScoreCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ScoringIntegrationTest {

    private final AdScoreCalculator scoreCalculator;
    private final AdRepository adRepository;

    @Autowired
    ScoringIntegrationTest(AdScoreCalculator scoreCalculator, AdRepository adRepository) {
        this.scoreCalculator = scoreCalculator;
        this.adRepository = adRepository;
    }

    @Test
    void scoreCalculator_shouldCalculateCorrectScoreForCompleteAd() {
        Ad completeAd = new Ad(
                999,
                Typology.FLAT,
                "Piso luminoso y céntrico reformado con acabados de lujo. Ubicado en una zona muy tranquila cerca de todos los servicios. Dispone de amplias habitaciones y mucha luz natural. Perfecto para familias que buscan comodidad y calidad de vida en el centro de la ciudad.",
                Arrays.asList(
                        new Picture(1, "http://example.com/1.jpg", Quality.HD),
                        new Picture(2, "http://example.com/2.jpg", Quality.HD)
                ),
                85,
                null
        );

        int score = scoreCalculator.calculate(completeAd);

        assertThat(score).isGreaterThanOrEqualTo(40);
        assertThat(completeAd.isComplete()).isTrue();
    }

    @Test
    void scoreCalculator_shouldCalculateLowScoreForIncompleteAd() {
        Ad incompleteAd = new Ad(
                999,
                Typology.FLAT,
                "",
                Collections.emptyList(),
                null,
                null
        );

        int score = scoreCalculator.calculate(incompleteAd);

        assertThat(score).isLessThan(40);
        assertThat(incompleteAd.isComplete()).isFalse();
    }

    @Test
    void scoreCalculator_shouldBonusForHDPhotos() {
        Ad adWithHDPhotos = new Ad(
                999,
                Typology.FLAT,
                "Descripción básica",
                Arrays.asList(
                        new Picture(1, "http://example.com/1.jpg", Quality.HD),
                        new Picture(2, "http://example.com/2.jpg", Quality.HD)
                ),
                50,
                null
        );

        Ad adWithSDPhotos = new Ad(
                998,
                Typology.FLAT,
                "Descripción básica",
                Arrays.asList(
                        new Picture(3, "http://example.com/3.jpg", Quality.SD),
                        new Picture(4, "http://example.com/4.jpg", Quality.SD)
                ),
                50,
                null
        );

        int scoreHD = scoreCalculator.calculate(adWithHDPhotos);
        int scoreSD = scoreCalculator.calculate(adWithSDPhotos);

        assertThat(scoreHD).isGreaterThan(scoreSD);
    }

    @Test
    void scoreCalculator_shouldBonusForKeywords() {
        Ad adWithKeywords = new Ad(
                999,
                Typology.FLAT,
                "Piso luminoso, céntrico y reformado en una ubicación única",
                Arrays.asList(new Picture(1, "http://example.com/1.jpg", Quality.HD)),
                50,
                null
        );

        Ad adWithoutKeywords = new Ad(
                998,
                Typology.FLAT,
                "Este es un piso normal sin palabras especiales que destacar",
                Arrays.asList(new Picture(2, "http://example.com/2.jpg", Quality.HD)),
                50,
                null
        );

        int scoreWithKeywords = scoreCalculator.calculate(adWithKeywords);
        int scoreWithoutKeywords = scoreCalculator.calculate(adWithoutKeywords);

        assertThat(scoreWithKeywords).isGreaterThan(scoreWithoutKeywords);
    }

    @Test
    void scoreCalculator_shouldRespectMinAndMaxBounds() {
        List<Ad> allAds = adRepository.findAllAds();

        for (Ad ad : allAds) {
            int score = scoreCalculator.calculate(ad);
            assertThat(score).isBetween(0, 100);
        }
    }

    @Test
    void garage_shouldNotRequireDescription() {
        Ad garage = new Ad(
                999,
                Typology.GARAGE,
                "",
                Arrays.asList(new Picture(1, "http://example.com/1.jpg", Quality.HD)),
                null,
                null
        );

        assertThat(garage.isComplete()).isTrue();

        int score = scoreCalculator.calculate(garage);
        assertThat(score).isGreaterThan(0);
    }

    @Test
    void chalet_shouldRequireGardenSize() {
        Ad chaletWithGarden = new Ad(
                999,
                Typology.CHALET,
                "Maravilloso chalet con jardín espacioso",
                Arrays.asList(new Picture(1, "http://example.com/1.jpg", Quality.HD)),
                200,
                100
        );

        Ad chaletWithoutGarden = new Ad(
                998,
                Typology.CHALET,
                "Maravilloso chalet con jardín espacioso",
                Arrays.asList(new Picture(2, "http://example.com/2.jpg", Quality.HD)),
                200,
                null
        );

        assertThat(chaletWithGarden.isComplete()).isTrue();
        assertThat(chaletWithoutGarden.isComplete()).isFalse();
    }

    @Test
    void flat_shouldNotRequireGardenSize() {
        Ad flat = new Ad(
                999,
                Typology.FLAT,
                "Piso céntrico y luminoso",
                Arrays.asList(new Picture(1, "http://example.com/1.jpg", Quality.HD)),
                80,
                null
        );

        assertThat(flat.isComplete()).isTrue();
    }
}
