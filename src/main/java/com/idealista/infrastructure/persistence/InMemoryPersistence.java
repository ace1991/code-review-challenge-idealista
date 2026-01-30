package com.idealista.infrastructure.persistence;

import com.idealista.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryPersistence implements AdRepository {

    private List<AdEntity> ads;
    private List<PictureEntity> pictures;

    public InMemoryPersistence() {
        ads = new ArrayList<>();
        ads.add(new AdEntity(1, "CHALET", "Este piso es una ganga, compra, compra, COMPRA!!!!!", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdEntity(2, "FLAT", "Nuevo ático céntrico recién reformado. No deje pasar la oportunidad y adquiera este ático de lujo", Arrays.asList(4), 300, null, null, null));
        ads.add(new AdEntity(3, "CHALET", "", Arrays.asList(2), 300, null, null, null));
        ads.add(new AdEntity(4, "FLAT", "Ático céntrico muy luminoso y recién reformado, parece nuevo", Arrays.asList(5), 300, null, null, null));
        ads.add(new AdEntity(5, "FLAT", "Pisazo,", Arrays.asList(3, 8), 300, null, null, null));
        ads.add(new AdEntity(6, "GARAGE", "", Arrays.asList(6), 300, null, null, null));
        ads.add(new AdEntity(7, "GARAGE", "Garaje en el centro de Albacete", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdEntity(8, "CHALET", "Maravilloso chalet situado en lAs afueras de un pequeño pueblo rural. El entorno es espectacular, las vistas magníficas. ¡Cómprelo ahora!", Arrays.asList(1, 7), 300, null, null, null));

        pictures = new ArrayList<>();
        pictures.add(new PictureEntity(1, "http://www.idealista.com/pictures/1", "SD"));
        pictures.add(new PictureEntity(2, "http://www.idealista.com/pictures/2", "HD"));
        pictures.add(new PictureEntity(3, "http://www.idealista.com/pictures/3", "SD"));
        pictures.add(new PictureEntity(4, "http://www.idealista.com/pictures/4", "HD"));
        pictures.add(new PictureEntity(5, "http://www.idealista.com/pictures/5", "SD"));
        pictures.add(new PictureEntity(6, "http://www.idealista.com/pictures/6", "SD"));
        pictures.add(new PictureEntity(7, "http://www.idealista.com/pictures/7", "SD"));
        pictures.add(new PictureEntity(8, "http://www.idealista.com/pictures/8", "HD"));
    }

    @Override
    public List<Ad> findAllAds() {
        return ads
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Ad ad) {
        ads.removeIf(x -> x.getId().equals(ad.getId()));
        ads.add(mapToPersistence(ad));

        ad.getPictures()
            .forEach(this::save);
    }

    private void save(Picture picture) {
        pictures.removeIf(x -> x.getId().equals(picture.getId()));
        pictures.add(mapToPersistence(picture));
    }

    @Override
    public List<Ad> findRelevantAds() {
        return ads
                .stream()
                .filter(x -> x.getScore() >= Constants.RELEVANCE_THRESHOLD)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ad> findIrrelevantAds() {
        return ads
                .stream()
                .filter(x -> x.getScore() < Constants.RELEVANCE_THRESHOLD)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Ad mapToDomain(AdEntity adEntity) {
        List<Picture> domainPictures = adEntity.getPictures().stream()
                .map(this::mapToDomain)
                .filter(picture -> picture != null)
                .collect(Collectors.toList());

        return new Ad(adEntity.getId(),
                Typology.valueOf(adEntity.getTypology()),
                adEntity.getDescription(),
                domainPictures,
                adEntity.getHouseSize(),
                adEntity.getGardenSize(),
                adEntity.getScore(),
                adEntity.getIrrelevantSince());
    }

    private Picture mapToDomain(Integer pictureId) {
        return pictures
                .stream()
                .filter(x -> x.getId().equals(pictureId))
                .findFirst()
                .map(pictureEntity -> new Picture(pictureEntity.getId(), pictureEntity.getUrl(), Quality.valueOf(pictureEntity.getQuality())))
                .orElseThrow(() -> new IllegalStateException("Picture not found with id: " + pictureId));
    }

    private AdEntity mapToPersistence(Ad ad) {
        return new AdEntity(ad.getId(),
                ad.getTypology().name(),
                ad.getDescription(),
                ad.getPictures().stream().map(Picture::getId).collect(Collectors.toList()),
                ad.getHouseSize(),
                ad.getGardenSize(),
                ad.getScore(),
                ad.getIrrelevantSince());
    }

    private PictureEntity mapToPersistence(Picture picture) {
        return new PictureEntity(picture.getId(),
                picture.getUrl(),
                picture.getQuality().name());
    }

}
