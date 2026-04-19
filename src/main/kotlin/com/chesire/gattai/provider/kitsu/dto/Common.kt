package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

/* Included */
data class KitsuIncludedDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("attributes")
    val attributes: KitsuIncludedAttributesDto
)

data class KitsuIncludedAttributesDto(
    @JsonProperty("externalSite")
    val externalSite: String,
    @JsonProperty("externalId")
    val externalId: String
)
