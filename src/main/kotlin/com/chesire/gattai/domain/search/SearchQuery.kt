package com.chesire.gattai.domain.search

import com.chesire.gattai.domain.SeriesType

data class SearchQuery(
    val title: String,
    val seriesType: SeriesType
)
