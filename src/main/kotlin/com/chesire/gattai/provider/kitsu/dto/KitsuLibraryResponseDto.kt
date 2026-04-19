package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class KitsuLibraryResponseDto(
    @JsonProperty("data")
    val data: List<KitsuSearchDataDto>,
    @JsonProperty("included")
    val included: List<KitsuIncludedDto>?
)

internal data class KitsuLibraryDataDto(
    @JsonProperty("id")
    val id: String
)
