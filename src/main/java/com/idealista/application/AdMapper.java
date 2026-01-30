package com.idealista.application;

import com.idealista.domain.Ad;
import com.idealista.domain.Picture;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AdMapper {

    public PublicAd toPublicAd(Ad ad) {
        PublicAd publicAd = new PublicAd();
        publicAd.setId(ad.getId());
        publicAd.setTypology(ad.getTypology().name());
        publicAd.setDescription(ad.getDescription());
        publicAd.setPictureUrls(ad.getPictures().stream()
                .map(Picture::getUrl)
                .collect(Collectors.toList()));
        publicAd.setHouseSize(ad.getHouseSize());
        publicAd.setGardenSize(ad.getGardenSize());
        return publicAd;
    }

    public QualityAd toQualityAd(Ad ad) {
        QualityAd qualityAd = new QualityAd();
        qualityAd.setId(ad.getId());
        qualityAd.setTypology(ad.getTypology().name());
        qualityAd.setDescription(ad.getDescription());
        qualityAd.setPictureUrls(ad.getPictures().stream()
                .map(Picture::getUrl)
                .collect(Collectors.toList()));
        qualityAd.setHouseSize(ad.getHouseSize());
        qualityAd.setGardenSize(ad.getGardenSize());
        qualityAd.setScore(ad.getScore());
        qualityAd.setIrrelevantSince(ad.getIrrelevantSince());
        return qualityAd;
    }
}
