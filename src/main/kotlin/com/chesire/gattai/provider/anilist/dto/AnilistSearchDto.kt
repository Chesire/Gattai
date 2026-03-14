package com.chesire.gattai.provider.anilist.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AnilistSearchDto(
    @JsonProperty("data")
    val data: AnilistDataDto
)

data class AnilistDataDto(
    @JsonProperty("Page")
    val page: AnilistPageDto
)

data class AnilistPageDto(
    @JsonProperty("media")
    val media: List<AnilistMediaDto>
)

data class AnilistMediaDto(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("title")
    val title: AnilistTitleDto
)

data class AnilistTitleDto(
    @JsonProperty("romaji")
    val romaji: String
)
