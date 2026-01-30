package com.idealista.domain.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class FlatScoreStrategyTest {

    private FlatScoreStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FlatScoreStrategy();
    }

    @Test
    void calculateDescriptionScore_withLessThan20Words_shouldReturn0() {
        // Given
        String description = String.join(" ", Collections.nCopies(15, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(0);
    }

    @Test
    void calculateDescriptionScore_with20Words_shouldReturn10() {
        // Given
        String description = String.join(" ", Collections.nCopies(20, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(10);
    }

    @Test
    void calculateDescriptionScore_with49Words_shouldReturn10() {
        // Given
        String description = String.join(" ", Collections.nCopies(49, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(10);
    }

    @Test
    void calculateDescriptionScore_with50Words_shouldReturn30() {
        // Given
        String description = String.join(" ", Collections.nCopies(50, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(30);
    }

    @Test
    void calculateDescriptionScore_withMoreThan50Words_shouldReturn30() {
        // Given
        String description = String.join(" ", Collections.nCopies(100, "palabra"));

        // When
        int score = strategy.calculateDescriptionScore(description);

        // Then
        assertThat(score).isEqualTo(30);
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
