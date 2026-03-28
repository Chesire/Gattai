package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.SeriesType
import jakarta.validation.constraints.NotNull

data class RetrieveLibraryParams(
    @NotNull
    val seriesType: SeriesType
)
