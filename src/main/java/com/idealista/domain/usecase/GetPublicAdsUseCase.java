package com.idealista.domain.usecase;

import com.idealista.infrastructure.api.PublicAd;

import java.util.List;

public interface GetPublicAdsUseCase {
    List<PublicAd> execute();
}
