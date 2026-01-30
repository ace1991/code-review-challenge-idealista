package com.idealista.domain.scoring;

import com.idealista.domain.Ad;

public interface ScoreCalculator {
    int calculate(Ad ad);
}
