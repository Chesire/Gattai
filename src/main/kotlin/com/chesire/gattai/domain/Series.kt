package com.chesire.gattai.domain

data class Series(
    val kitsuId: String?,
    val malId: String?,
    val aniListId: String?,
    val titles: List<String>,
    val seriesType: SeriesType
)

enum class SeriesType {
    ANIME,
    MANGA
}
