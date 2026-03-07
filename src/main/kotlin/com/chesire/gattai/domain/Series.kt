package com.chesire.gattai.domain

data class Series(
    val kitsuId: Int?,
    val malId: Int?,
    val aniListId: Int?,
    val title: String,
    val seriesType: SeriesType
)

enum class SeriesType {
    ANIME,
    MANGA
}
