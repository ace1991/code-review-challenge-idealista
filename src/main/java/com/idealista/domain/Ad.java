package com.idealista.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ad {

    private Integer id;
    private Typology typology;
    private String description;
    private List<Picture> pictures;
    private Integer houseSize;
    private Integer gardenSize;
    private Integer score;
    private Date irrelevantSince;

    public Ad(Integer id,
              Typology typology,
              String description,
              List<Picture> pictures,
              Integer houseSize,
              Integer gardenSize) {
        this(id, typology, description, pictures, houseSize, gardenSize, null, null);
    }

    public boolean isComplete() {
        if (!hasPictures()) {
            return false;
        }

        switch (typology) {
            case GARAGE:
                return isGarageComplete();
            case FLAT:
                return isFlatComplete();
            case CHALET:
                return isChaletComplete();
            default:
                return false;
        }
    }

    private boolean hasPictures() {
        return pictures != null && !pictures.isEmpty();
    }

    private boolean hasValidDescription() {
        return description != null && !description.isEmpty();
    }

    private boolean isGarageComplete() {
        return hasPictures();
    }

    private boolean isFlatComplete() {
        return hasPictures()
            && hasValidDescription()
            && houseSize != null;
    }

    private boolean isChaletComplete() {
        return hasPictures()
            && hasValidDescription()
            && houseSize != null
            && gardenSize != null;
    }
}
