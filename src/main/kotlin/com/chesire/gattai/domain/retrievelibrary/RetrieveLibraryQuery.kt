package com.chesire.gattai.domain.retrievelibrary

import com.chesire.gattai.domain.SeriesType

data class RetrieveLibraryQuery(
    val token: String,
    val seriesType: SeriesType
)
