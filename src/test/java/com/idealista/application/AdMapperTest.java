package com.idealista.application;

import com.idealista.domain.Ad;
import com.idealista.domain.Picture;
import com.idealista.domain.Quality;
import com.idealista.domain.Typology;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdMapperTest {

    private AdMapper adMapper;

    @BeforeEach
    void setUp() {
        adMapper = new AdMapper();
    }

    @Test
    void toPublicAd_shouldMapAllFields() {
        // Given
        List<Picture> pictures = Arrays.asList(
                new Picture(1, "http://example.com/1.jpg", Quality.HD),
                new Picture(2, "http://example.com/2.jpg", Quality.SD)
        );

        Ad ad = new Ad(
                123,
                Typology.FLAT,
                "Descripción del piso",
                pictures,
                85,
                null,
                75,
                null
        );

        // When
        PublicAd result = adMapper.toPublicAd(ad);

        // Then
        assertThat(result.getId()).isEqualTo(123);
        assertThat(result.getTypology()).isEqualTo("FLAT");
        assertThat(result.getDescription()).isEqualTo("Descripción del piso");
        assertThat(result.getHouseSize()).isEqualTo(85);
        assertThat(result.getGardenSize()).isNull();
        assertThat(result.getPictureUrls()).containsExactly(
                "http://example.com/1.jpg",
                "http://example.com/2.jpg"
        );
    }

    @Test
    void toQualityAd_shouldMapAllFieldsIncludingScoreAndDate() {
        // Given
        Date irrelevantDate = new Date();
        List<Picture> pictures = Arrays.asList(
                new Picture(1, "http://example.com/1.jpg", Quality.HD)
        );

        Ad ad = new Ad(
                456,
                Typology.CHALET,
                "Descripción del chalet",
                pictures,
                150,
                80,
                25,
                irrelevantDate
        );

        // When
        QualityAd result = adMapper.toQualityAd(ad);

        // Then
        assertThat(result.getId()).isEqualTo(456);
        assertThat(result.getTypology()).isEqualTo("CHALET");
        assertThat(result.getDescription()).isEqualTo("Descripción del chalet");
        assertThat(result.getHouseSize()).isEqualTo(150);
        assertThat(result.getGardenSize()).isEqualTo(80);
        assertThat(result.getScore()).isEqualTo(25);
        assertThat(result.getIrrelevantSince()).isEqualTo(irrelevantDate);
        assertThat(result.getPictureUrls()).containsExactly("http://example.com/1.jpg");
    }

    @Test
    void toPublicAd_withGarage_shouldMapCorrectly() {
        // Given
        List<Picture> pictures = Arrays.asList(
                new Picture(1, "http://example.com/garage.jpg", Quality.HD)
        );

        Ad ad = new Ad(
                789,
                Typology.GARAGE,
                null,
                pictures,
                null,
                null,
                60,
                null
        );

        // When
        PublicAd result = adMapper.toPublicAd(ad);

        // Then
        assertThat(result.getId()).isEqualTo(789);
        assertThat(result.getTypology()).isEqualTo("GARAGE");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getHouseSize()).isNull();
        assertThat(result.getGardenSize()).isNull();
        assertThat(result.getPictureUrls()).containsExactly("http://example.com/garage.jpg");
    }
}
