package com.idealista.domain.scoring;

public interface TypologyScoreStrategy {
    int calculateDescriptionScore(String description);
}
