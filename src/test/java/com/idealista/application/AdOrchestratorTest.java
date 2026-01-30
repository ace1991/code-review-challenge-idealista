package com.idealista.application;

import com.idealista.domain.usecase.CalculateAdScoresUseCase;
import com.idealista.domain.usecase.GetPublicAdsUseCase;
import com.idealista.domain.usecase.GetQualityAdsUseCase;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdOrchestratorTest {

    @Mock
    private GetPublicAdsUseCase getPublicAdsUseCase;

    @Mock
    private GetQualityAdsUseCase getQualityAdsUseCase;

    @Mock
    private CalculateAdScoresUseCase calculateAdScoresUseCase;

    private AdOrchestrator adOrchestrator;

    @BeforeEach
    void setUp() {
        adOrchestrator = new AdOrchestrator(getPublicAdsUseCase, getQualityAdsUseCase, calculateAdScoresUseCase);
    }

    @Test
    void findPublicAds_shouldDelegateToUseCase() {
        List<PublicAd> expectedAds = Arrays.asList(new PublicAd(), new PublicAd());
        when(getPublicAdsUseCase.execute()).thenReturn(expectedAds);

        List<PublicAd> result = adOrchestrator.findPublicAds();

        verify(getPublicAdsUseCase).execute();
        assertThat(result).isEqualTo(expectedAds);
    }

    @Test
    void findQualityAds_shouldDelegateToUseCase() {
        List<QualityAd> expectedAds = Collections.singletonList(new QualityAd());
        when(getQualityAdsUseCase.execute()).thenReturn(expectedAds);

        List<QualityAd> result = adOrchestrator.findQualityAds();

        verify(getQualityAdsUseCase).execute();
        assertThat(result).isEqualTo(expectedAds);
    }

    @Test
    void calculateScores_shouldDelegateToUseCase() {
        adOrchestrator.calculateScores();

        verify(calculateAdScoresUseCase).execute();
    }
}
