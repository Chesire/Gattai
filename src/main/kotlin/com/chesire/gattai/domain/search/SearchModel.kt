package com.chesire.gattai.domain.search

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType

data class SearchModel(
    val ids: Ids,
    val title: String,
    val seriesType: SeriesType
)
