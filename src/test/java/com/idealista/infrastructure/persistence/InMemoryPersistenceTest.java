package com.idealista.infrastructure.persistence;

import com.idealista.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryPersistenceTest {

    private InMemoryPersistence persistence;

    @BeforeEach
    void setUp() {
        persistence = new InMemoryPersistence();
    }

    @Test
    void findAllAds_shouldReturnAllAds() {
        List<Ad> ads = persistence.findAllAds();

        assertThat(ads).hasSize(8);
        assertThat(ads).allMatch(ad -> ad.getId() != null);
    }

    @Test
    void findRelevantAds_shouldReturnOnlyAdsWithScoreAboveThreshold() {
        persistence.findAllAds().forEach(ad -> {
            ad.setScore(50);
            persistence.save(ad);
        });

        List<Ad> relevantAds = persistence.findRelevantAds();

        assertThat(relevantAds).isNotEmpty();
        assertThat(relevantAds).allMatch(ad -> ad.getScore() != null && ad.getScore() >= Constants.RELEVANCE_THRESHOLD);
    }

    @Test
    void findIrrelevantAds_shouldReturnOnlyAdsWithScoreBelowThreshold() {
        persistence.findAllAds().forEach(ad -> {
            ad.setScore(20);
            persistence.save(ad);
        });

        List<Ad> irrelevantAds = persistence.findIrrelevantAds();

        assertThat(irrelevantAds).isNotEmpty();
        assertThat(irrelevantAds).allMatch(ad -> ad.getScore() < Constants.RELEVANCE_THRESHOLD);
    }

    @Test
    void save_shouldUpdateExistingAd() {
        List<Ad> ads = persistence.findAllAds();
        Ad adToUpdate = ads.get(0);
        Integer originalId = adToUpdate.getId();

        adToUpdate.setScore(75);
        adToUpdate.setIrrelevantSince(new Date());

        persistence.save(adToUpdate);

        List<Ad> updatedAds = persistence.findAllAds();
        assertThat(updatedAds).hasSize(8);

        Ad updatedAd = updatedAds.stream()
                .filter(ad -> ad.getId().equals(originalId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Ad not found"));

        assertThat(updatedAd.getScore()).isEqualTo(75);
        assertThat(updatedAd.getIrrelevantSince()).isNotNull();
    }

    @Test
    void mapToDomain_shouldMapAllFieldsCorrectly() {
        List<Ad> ads = persistence.findAllAds();

        Ad firstAd = ads.get(0);
        assertThat(firstAd.getId()).isEqualTo(1);
        assertThat(firstAd.getTypology()).isEqualTo(Typology.CHALET);
        assertThat(firstAd.getDescription()).isNotNull();
    }

    @Test
    void mapToDomain_shouldMapPicturesCorrectly() {
        List<Ad> ads = persistence.findAllAds();

        Ad adWithPictures = ads.stream()
                .filter(ad -> !ad.getPictures().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("No ad with pictures found"));

        assertThat(adWithPictures.getPictures()).isNotEmpty();
        assertThat(adWithPictures.getPictures().get(0)).isNotNull();
        assertThat(adWithPictures.getPictures().get(0).getUrl()).isNotNull();
        assertThat(adWithPictures.getPictures().get(0).getQuality()).isNotNull();
    }

    @Test
    void mapToDomain_shouldFilterNonExistentPictures() {
        List<Ad> originalAds = persistence.findAllAds();

        Ad adWithPictures = originalAds.stream()
                .filter(ad -> !ad.getPictures().isEmpty())
                .findFirst()
                .orElseThrow(() -> new AssertionError("No ad with pictures found"));

        assertThat(adWithPictures.getPictures()).isNotEmpty();
        assertThat(adWithPictures.getPictures()).allMatch(picture -> picture.getUrl() != null);
    }

    @Test
    void initialData_shouldHaveCorrectTypologies() {
        List<Ad> ads = persistence.findAllAds();

        long chaletCount = ads.stream().filter(ad -> ad.getTypology() == Typology.CHALET).count();
        long flatCount = ads.stream().filter(ad -> ad.getTypology() == Typology.FLAT).count();
        long garageCount = ads.stream().filter(ad -> ad.getTypology() == Typology.GARAGE).count();

        assertThat(chaletCount).isEqualTo(3);
        assertThat(flatCount).isEqualTo(3);
        assertThat(garageCount).isEqualTo(2);
    }

    @Test
    void initialData_shouldHavePicturesWithCorrectQuality() {
        List<Ad> ads = persistence.findAllAds();

        List<Picture> allPictures = ads.stream()
                .flatMap(ad -> ad.getPictures().stream())
                .collect(java.util.stream.Collectors.toList());

        long hdPictures = allPictures.stream().filter(p -> p.getQuality() == Quality.HD).count();
        long sdPictures = allPictures.stream().filter(p -> p.getQuality() == Quality.SD).count();

        assertThat(hdPictures).isGreaterThan(0);
        assertThat(sdPictures).isGreaterThan(0);
        assertThat(allPictures).hasSize(8);
    }
}
