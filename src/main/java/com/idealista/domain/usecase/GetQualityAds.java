package com.idealista.domain.usecase;

import com.idealista.application.AdMapper;
import com.idealista.domain.AdRepository;
import com.idealista.infrastructure.api.QualityAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetQualityAds implements GetQualityAdsUseCase {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    @Autowired
    public GetQualityAds(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public List<QualityAd> execute() {
        return adRepository.findIrrelevantAds().stream()
                .map(adMapper::toQualityAd)
                .collect(Collectors.toList());
    }
}
