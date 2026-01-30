package com.idealista.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AdTest {

    @Test
    void isComplete_garage_shouldBeTrueWhenHasPictures() {
        // Given
        Ad garage = new Ad(1, Typology.GARAGE, null,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                null, null);

        // When
        boolean isComplete = garage.isComplete();

        // Then
        assertThat(isComplete).isTrue();
    }

    @Test
    void isComplete_garage_shouldBeFalseWhenNoPictures() {
        // Given
        Ad garage = new Ad(1, Typology.GARAGE, "Descripción", Collections.emptyList(), null, null);

        // When
        boolean isComplete = garage.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_flat_shouldBeTrueWhenHasAllRequiredFields() {
        // Given
        Ad flat = new Ad(1, Typology.FLAT, "Descripción del piso",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        boolean isComplete = flat.isComplete();

        // Then
        assertThat(isComplete).isTrue();
    }

    @Test
    void isComplete_flat_shouldBeFalseWhenMissingPictures() {
        // Given
        Ad flat = new Ad(1, Typology.FLAT, "Descripción del piso",
                Collections.emptyList(), 50, null);

        // When
        boolean isComplete = flat.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_flat_shouldBeFalseWhenMissingDescription() {
        // Given
        Ad flat = new Ad(1, Typology.FLAT, null,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        boolean isComplete = flat.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_flat_shouldBeFalseWhenDescriptionIsEmpty() {
        // Given
        Ad flat = new Ad(1, Typology.FLAT, "",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                50, null);

        // When
        boolean isComplete = flat.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_flat_shouldBeFalseWhenMissingHouseSize() {
        // Given
        Ad flat = new Ad(1, Typology.FLAT, "Descripción",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                null, null);

        // When
        boolean isComplete = flat.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_chalet_shouldBeTrueWhenHasAllRequiredFields() {
        // Given
        Ad chalet = new Ad(1, Typology.CHALET, "Descripción del chalet",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                100, 50);

        // When
        boolean isComplete = chalet.isComplete();

        // Then
        assertThat(isComplete).isTrue();
    }

    @Test
    void isComplete_chalet_shouldBeFalseWhenMissingGardenSize() {
        // Given
        Ad chalet = new Ad(1, Typology.CHALET, "Descripción del chalet",
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                100, null);

        // When
        boolean isComplete = chalet.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_chalet_shouldBeFalseWhenMissingPictures() {
        // Given
        Ad chalet = new Ad(1, Typology.CHALET, "Descripción del chalet",
                Collections.emptyList(), 100, 50);

        // When
        boolean isComplete = chalet.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_chalet_shouldBeFalseWhenMissingDescription() {
        // Given
        Ad chalet = new Ad(1, Typology.CHALET, null,
                Collections.singletonList(new Picture(1, "url", Quality.HD)),
                100, 50);

        // When
        boolean isComplete = chalet.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void isComplete_shouldHandleNullPicturesList() {
        // Given
        Ad ad = new Ad(1, Typology.FLAT, "Descripción", null, 50, null);

        // When
        boolean isComplete = ad.isComplete();

        // Then
        assertThat(isComplete).isFalse();
    }

    @Test
    void equals_shouldReturnTrueForSameContent() {
        // Given
        Picture picture = new Picture(1, "url", Quality.HD);
        Ad ad1 = new Ad(1, Typology.FLAT, "Desc", Collections.singletonList(picture), 50, null, 75, null);
        Ad ad2 = new Ad(1, Typology.FLAT, "Desc", Collections.singletonList(picture), 50, null, 75, null);

        // When/Then
        assertThat(ad1).isEqualTo(ad2);
        assertThat(ad1.hashCode()).isEqualTo(ad2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        // Given
        Ad ad1 = new Ad(1, Typology.FLAT, "Desc", Collections.emptyList(), 50, null);
        Ad ad2 = new Ad(2, Typology.FLAT, "Desc", Collections.emptyList(), 50, null);

        // When/Then
        assertThat(ad1).isNotEqualTo(ad2);
    }
}
