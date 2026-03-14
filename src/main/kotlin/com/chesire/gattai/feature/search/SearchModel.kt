package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.SeriesType

data class SearchModel(
    val ids: Ids,
    val title: String,
    val seriesType: SeriesType
)

data class Ids(
    val kitsuId: String? = null,
    val malId: String? = null,
    val anilistId: String? = null
)
