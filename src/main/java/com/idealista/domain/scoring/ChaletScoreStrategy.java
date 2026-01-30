package com.idealista.domain.scoring;

public class ChaletScoreStrategy implements TypologyScoreStrategy {

    private static final int MIN_WORDS_FOR_BONUS = 50;
    private static final int LARGE_DESCRIPTION_POINTS = 20;

    @Override
    public int calculateDescriptionScore(String description) {
        int wordCount = countWords(description);

        if (wordCount > MIN_WORDS_FOR_BONUS) {
            return LARGE_DESCRIPTION_POINTS;
        }

        return 0;
    }

    private int countWords(String description) {
        if (description == null || description.trim().isEmpty()) {
            return 0;
        }
        return description.trim().split("\\s+").length;
    }
}
