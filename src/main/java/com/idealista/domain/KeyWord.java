package com.idealista.domain;

public enum KeyWord {
    LUMINOSO("luminoso", 5),
    NUEVO("nuevo", 5),
    CENTRICO("céntrico", 5),
    REFORMADO("reformado", 5),
    ATICO("ático", 5);

    private final String word;
    private final int points;

    KeyWord(String word, int points) {
        this.word = word;
        this.points = points;
    }

    public String getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }
}
