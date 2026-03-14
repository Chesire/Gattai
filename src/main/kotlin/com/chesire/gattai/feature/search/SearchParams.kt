package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.SeriesType

data class SearchParams(
    val title: String,
    val seriesType: SeriesType = SeriesType.ANIME
) {

    val isEmpty: Boolean
        get() = title.isBlank()
}
