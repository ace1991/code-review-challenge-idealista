package com.idealista.domain.scoring;

public class FlatScoreStrategy implements TypologyScoreStrategy {

    private static final int MIN_WORDS_FOR_SMALL_BONUS = 20;
    private static final int MAX_WORDS_FOR_SMALL_BONUS = 49;
    private static final int MIN_WORDS_FOR_LARGE_BONUS = 50;
    private static final int SMALL_DESCRIPTION_POINTS = 10;
    private static final int LARGE_DESCRIPTION_POINTS = 30;

    @Override
    public int calculateDescriptionScore(String description) {
        int wordCount = countWords(description);

        if (wordCount >= MIN_WORDS_FOR_LARGE_BONUS) {
            return LARGE_DESCRIPTION_POINTS;
        } else if (wordCount >= MIN_WORDS_FOR_SMALL_BONUS && wordCount <= MAX_WORDS_FOR_SMALL_BONUS) {
            return SMALL_DESCRIPTION_POINTS;
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
