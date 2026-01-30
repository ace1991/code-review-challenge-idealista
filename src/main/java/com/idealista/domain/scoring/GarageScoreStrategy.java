package com.idealista.domain.scoring;

public class GarageScoreStrategy implements TypologyScoreStrategy {

    @Override
    public int calculateDescriptionScore(String description) {
        // Los garajes no obtienen puntos extra por descripción
        return 0;
    }
}
