package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.SeriesType

data class SearchParams(
    val title: String,
    val seriesType: SeriesType? = null
)