package com.idealista.domain.usecase;

import com.idealista.application.AdMapper;
import com.idealista.domain.Ad;
import com.idealista.domain.AdRepository;
import com.idealista.infrastructure.api.PublicAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPublicAds implements GetPublicAdsUseCase {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    @Autowired
    public GetPublicAds(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public List<PublicAd> execute() {
        return adRepository.findRelevantAds().stream()
                .sorted(Comparator.comparing(Ad::getScore).reversed())
                .map(adMapper::toPublicAd)
                .collect(Collectors.toList());
    }
}
