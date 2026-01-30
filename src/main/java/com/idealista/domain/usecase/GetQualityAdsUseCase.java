package com.idealista.domain.usecase;

import com.idealista.infrastructure.api.QualityAd;

import java.util.List;

public interface GetQualityAdsUseCase {
    List<QualityAd> execute();
}
