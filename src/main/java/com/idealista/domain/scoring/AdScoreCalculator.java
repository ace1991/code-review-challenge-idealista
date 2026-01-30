package com.idealista.domain.scoring;

import com.idealista.domain.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AdScoreCalculator implements ScoreCalculator {

    private static final int NO_PHOTOS_PENALTY = -10;
    private static final int HD_PHOTO_POINTS = 20;
    private static final int SD_PHOTO_POINTS = 10;
    private static final int HAS_DESCRIPTION_POINTS = 5;
    private static final int COMPLETE_AD_POINTS = 40;
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;

    private final TypologyScoreStrategy flatStrategy;
    private final TypologyScoreStrategy chaletStrategy;
    private final TypologyScoreStrategy garageStrategy;

    public AdScoreCalculator() {
        this.flatStrategy = new FlatScoreStrategy();
        this.chaletStrategy = new ChaletScoreStrategy();
        this.garageStrategy = new GarageScoreStrategy();
    }

    @Override
    public int calculate(Ad ad) {
        int score = 0;

        score += calculatePicturesScore(ad);
        score += calculateDescriptionScore(ad);
        score += calculateCompletenessScore(ad);

        return Math.max(MIN_SCORE, Math.min(MAX_SCORE, score));
    }

    private int calculatePicturesScore(Ad ad) {
        if (ad.getPictures().isEmpty()) {
            return NO_PHOTOS_PENALTY;
        }

        return ad.getPictures().stream()
                .mapToInt(picture -> Quality.HD.equals(picture.getQuality())
                        ? HD_PHOTO_POINTS
                        : SD_PHOTO_POINTS)
                .sum();
    }

    private int calculateDescriptionScore(Ad ad) {
        return Optional.ofNullable(ad.getDescription())
                .filter(desc -> !desc.isEmpty())
                .map(description -> {
                    int score = HAS_DESCRIPTION_POINTS;
                    score += getTypologyStrategy(ad.getTypology()).calculateDescriptionScore(description);
                    score += calculateKeyWordsScore(description);
                    return score;
                })
                .orElse(0);
    }

    private int calculateKeyWordsScore(String description) {
        List<String> words = Arrays.asList(description.toLowerCase().split("\\s+"));

        return Arrays.stream(KeyWord.values())
                .filter(keyWord -> words.contains(keyWord.getWord().toLowerCase()))
                .mapToInt(KeyWord::getPoints)
                .sum();
    }

    private int calculateCompletenessScore(Ad ad) {
        return ad.isComplete() ? COMPLETE_AD_POINTS : 0;
    }

    private TypologyScoreStrategy getTypologyStrategy(Typology typology) {
        switch (typology) {
            case FLAT:
                return flatStrategy;
            case CHALET:
                return chaletStrategy;
            case GARAGE:
                return garageStrategy;
            default:
                throw new IllegalArgumentException("Unknown typology: " + typology);
        }
    }
}
