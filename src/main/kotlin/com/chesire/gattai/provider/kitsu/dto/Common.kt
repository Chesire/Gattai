package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KitsuIncludedAttributesDto(
    @JsonProperty("externalSite")
    val externalSite: String,
    @JsonProperty("externalId")
    val externalId: String
)
