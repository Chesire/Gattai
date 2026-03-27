package com.chesire.gattai.provider.anilist.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AnilistQueryDto(
    @JsonProperty("query")
    val query: String,
    @JsonProperty("variables")
    val variables: AnilistQueryVariablesDto
)

data class AnilistQueryVariablesDto(
    @JsonProperty("search")
    val search: String,
    @JsonProperty("type")
    val type: String
)
