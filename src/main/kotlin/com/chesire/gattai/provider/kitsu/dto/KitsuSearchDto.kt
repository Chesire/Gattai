package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class KitsuSearchDto(
    @JsonProperty("data")
    val data: List<KitsuSearchModelDto>
)

data class KitsuSearchModelDto(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("type")
    val type: String, // TODO: Use enum
    @JsonProperty("attributes")
    val attributes: KitsuSearchModelAttributesDto
)

data class KitsuSearchModelAttributesDto(
    @JsonProperty("canonicalTitle")
    val canonicalTitle: String
)
