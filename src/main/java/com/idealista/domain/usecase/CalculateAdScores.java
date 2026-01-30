package com.idealista.domain.usecase;
import com.idealista.domain.Ad;
import com.idealista.domain.AdRepository;
import com.idealista.domain.scoring.ScoreCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
@Service
public class CalculateAdScores implements CalculateAdScoresUseCase {
    private static final int RELEVANCE_THRESHOLD = 40;
    private final AdRepository adRepository;
    private final ScoreCalculator scoreCalculator;
    @Autowired
    public CalculateAdScores(AdRepository adRepository, ScoreCalculator scoreCalculator) {
        this.adRepository = adRepository;
        this.scoreCalculator = scoreCalculator;
    }
    @Override
    public void execute() {
        adRepository.findAllAds()
                .forEach(this::calculateAndUpdateScore);
    }
    private void calculateAndUpdateScore(Ad ad) {
        int score = scoreCalculator.calculate(ad);
        ad.setScore(score);
        if (score < RELEVANCE_THRESHOLD) {
            if (ad.getIrrelevantSince() == null) {
                ad.setIrrelevantSince(new Date());
            }
        } else {
            ad.setIrrelevantSince(null);
        }
        adRepository.save(ad);
    }
}

