package com.idealista.domain.scoring;

import com.idealista.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AdScoreCalculatorTest {

    private AdScoreCalculator scoreCalculator;

    @BeforeEach
    void setUp() {
        scoreCalculator = new AdScoreCalculator();
    }

    @Test
    void calculate_withNoPhotos_shouldSubtract10Points() {
        // Given
        Ad ad = new Ad(1, Typology.FLAT, "Descripción", Collections.emptyList(), 50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(0); // -10 + 5 (descripción) = -5, pero mínimo es 0
    }

    @Test
    void calculate_withHDPhotos_shouldAdd20PointsPerPhoto() {
        // Given
        Ad ad = new Ad(1, Typology.GARAGE, null,
                Arrays.asList(
                        new Picture(1, "url1", Quality.HD),
                        new Picture(2, "url2", Quality.HD)
                ),
                null, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(80); // 20 + 20 + 40 (completo)
    }

    @Test
    void calculate_withSDPhotos_shouldAdd10PointsPerPhoto() {
        // Given
        Ad ad = new Ad(1, Typology.GARAGE, null,
                Arrays.asList(
                        new Picture(1, "url1", Quality.SD),
                        new Picture(2, "url2", Quality.SD)
                ),
                null, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(60); // 10 + 10 + 40 (completo)
    }

    @Test
    void calculate_withDescription_shouldAdd5Points() {
        // Given
        Ad ad = new Ad(1, Typology.GARAGE, "Una descripción",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                null, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(65); // 20 (foto HD) + 5 (descripción) + 40 (completo)
    }

    @Test
    void calculate_flatWith20to49Words_shouldAdd10ExtraPoints() {
        // Given
        String description = String.join(" ", Collections.nCopies(30, "palabra"));
        Ad ad = new Ad(1, Typology.FLAT, description,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 10 (20-49 palabras) + 40 (completo) = 75
        assertThat(score).isEqualTo(75);
    }

    @Test
    void calculate_flatWith50OrMoreWords_shouldAdd30ExtraPoints() {
        // Given
        String description = String.join(" ", Collections.nCopies(55, "palabra"));
        Ad ad = new Ad(1, Typology.FLAT, description,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 30 (50+ palabras) + 40 (completo) = 95
        assertThat(score).isEqualTo(95);
    }

    @Test
    void calculate_chaletWithMoreThan50Words_shouldAdd20ExtraPoints() {
        // Given
        String description = String.join(" ", Collections.nCopies(55, "palabra"));
        Ad ad = new Ad(1, Typology.CHALET, description,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                100, 50);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 20 (50+ palabras) + 40 (completo) = 85
        assertThat(score).isEqualTo(85);
    }

    @Test
    void calculate_withKeyWords_shouldAdd5PointsPerKeyWord() {
        // Given
        String description = "Piso luminoso y nuevo en zona céntrico";
        Ad ad = new Ad(1, Typology.FLAT, description,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 15 (3 palabras clave) + 40 (completo) = 80
        assertThat(score).isEqualTo(80);
    }

    @Test
    void calculate_completeFlat_shouldAdd40Points() {
        // Given
        Ad ad = new Ad(1, Typology.FLAT, "Descripción",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 40 (completo) = 65
        assertThat(score).isEqualTo(65);
    }

    @Test
    void calculate_completeChalet_shouldAdd40Points() {
        // Given
        Ad ad = new Ad(1, Typology.CHALET, "Descripción",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                100, 50);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 5 (descripción) + 40 (completo) = 65
        assertThat(score).isEqualTo(65);
    }

    @Test
    void calculate_completeGarage_shouldAdd40Points() {
        // Given - garaje completo (solo necesita fotos, no descripción)
        Ad ad = new Ad(1, Typology.GARAGE, null,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                null, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        // 20 (foto) + 40 (completo) = 60
        assertThat(score).isEqualTo(60);
    }

    @Test
    void calculate_shouldNotExceed100Points() {
        // Given - anuncio con muchos puntos
        String description = String.join(" ", Collections.nCopies(60, "luminoso"));
        Ad ad = new Ad(1, Typology.FLAT, description,
                Arrays.asList(
                        new Picture(1, "url1", Quality.HD),
                        new Picture(2, "url2", Quality.HD),
                        new Picture(3, "url3", Quality.HD),
                        new Picture(4, "url4", Quality.HD),
                        new Picture(5, "url5", Quality.HD)
                ),
                50, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(100);
    }

    @Test
    void calculate_shouldNotBeLessThan0() {
        // Given - anuncio sin fotos ni nada
        Ad ad = new Ad(1, Typology.FLAT, null, Collections.emptyList(), null, null);

        // When
        int score = scoreCalculator.calculate(ad);

        // Then
        assertThat(score).isEqualTo(0);
    }
}
