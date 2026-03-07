package com.chesire.gattai.provider.mapping

import com.fasterxml.jackson.annotation.JsonProperty

data class SeriesIdMappingEntry(
    @JsonProperty("kitsu_id")
    val kitsuId: Int? = null,
    @JsonProperty("mal_id")
    val malId: Int? = null,
    @JsonProperty("anilist_id")
    val anilistId: Int? = null
)
