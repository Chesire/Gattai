package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.SeriesType

data class SearchModel(
    val id: Int,
    val targetType: TargetType,
    val title: String,
    val seriesType: SeriesType
)

enum class TargetType {
    KITSU,
    MAL,
    ANILIST
}
