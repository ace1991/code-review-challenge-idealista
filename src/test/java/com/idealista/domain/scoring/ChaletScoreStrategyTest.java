package com.idealista.domain.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ChaletScoreStrategyTest {

    private ChaletScoreStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ChaletScoreStrategy();
    }

    @Test
    void calculateDescriptionScore_with50Words_shouldReturn0() {
        // Given
        String description = String.join(" ", Collections.nCopies(50, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(0);
    }

    @Test
    void calculateDescriptionScore_with51Words_shouldReturn20() {
        // Given
        String description = String.join(" ", Collections.nCopies(51, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(20);
    }

    @Test
    void calculateDescriptionScore_withMoreThan50Words_shouldReturn20() {
        // Given
        String description = String.join(" ", Collections.nCopies(100, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(20);
    }

    @Test
    void calculateDescriptionScore_withLessThan50Words_shouldReturn0() {
        // Given
        String description = String.join(" ", Collections.nCopies(30, "palabra"));

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

    @Test
    void calculateDescriptionScore_withEmptyDescription_shouldReturn0() {
        // When
        int score = strategy.calculateDescriptionScore("");

        // Then
        assertThat(score).isEqualTo(0);
    }
}
