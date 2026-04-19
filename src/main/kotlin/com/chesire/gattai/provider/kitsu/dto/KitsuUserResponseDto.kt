package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class KitsuUserResponseDto(
    @JsonProperty("data")
    val data: List<KitsuUserDataDto>
)

internal data class KitsuUserDataDto(
    @JsonProperty("id")
    val id: String
)
