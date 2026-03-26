package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.SeriesType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SearchParams(
    @NotBlank
    @field:Size(max = 255)
    val title: String,
    val seriesType: SeriesType = SeriesType.ANIME
)
