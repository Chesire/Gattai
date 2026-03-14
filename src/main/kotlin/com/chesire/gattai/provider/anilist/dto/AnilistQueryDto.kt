package com.chesire.gattai.provider.anilist.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AnilistQueryDto(
    @JsonProperty("query")
    val query: String
)
