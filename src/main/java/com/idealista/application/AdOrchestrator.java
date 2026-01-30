package com.idealista.application;

import com.idealista.domain.usecase.CalculateAdScoresUseCase;
import com.idealista.domain.usecase.GetPublicAdsUseCase;
import com.idealista.domain.usecase.GetQualityAdsUseCase;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdOrchestrator implements AdsService {

    private final GetPublicAdsUseCase getPublicAdsUseCase;
    private final GetQualityAdsUseCase getQualityAdsUseCase;
    private final CalculateAdScoresUseCase calculateAdScoresUseCase;

    @Autowired
    public AdOrchestrator(GetPublicAdsUseCase getPublicAdsUseCase,
                          GetQualityAdsUseCase getQualityAdsUseCase,
                          CalculateAdScoresUseCase calculateAdScoresUseCase) {
        this.getPublicAdsUseCase = getPublicAdsUseCase;
        this.getQualityAdsUseCase = getQualityAdsUseCase;
        this.calculateAdScoresUseCase = calculateAdScoresUseCase;
    }

    @Override
    public List<PublicAd> findPublicAds() {
        return getPublicAdsUseCase.execute();
    }

    @Override
    public List<QualityAd> findQualityAds() {
        return getQualityAdsUseCase.execute();
    }

    @Override
    public void calculateScores() {
        calculateAdScoresUseCase.execute();
    }
}

