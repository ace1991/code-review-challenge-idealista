package com.idealista.domain.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GarageScoreStrategyTest {

    private GarageScoreStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GarageScoreStrategy();
    }

    @Test
    void calculateDescriptionScore_shouldAlwaysReturn0() {
        // Given
        String description = "Una descripción muy larga con muchas palabras";

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(0);
    }

    @Test
    void calculateDescriptionScore_withNullDescription_shouldReturn0() {
        // When
        int score = strategy.calculateDescriptionScore(null);

        // Then
        assertThat(score).isEqualTo(0);
    }
}
