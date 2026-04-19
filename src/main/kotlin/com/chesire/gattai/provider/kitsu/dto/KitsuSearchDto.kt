package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class KitsuSearchDto(
    @JsonProperty("data")
    val data: List<KitsuSearchDataDto>,
    @JsonProperty("included")
    val included: List<KitsuIncludedDto>?
)

data class KitsuSearchDataDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("attributes")
    val attributes: KitsuSearchDataAttributesDto,
    @JsonProperty("relationships")
    val relationships: KitsuSearchRelationshipsDto?
)

/* Attributes */
data class KitsuSearchDataAttributesDto(
    @JsonProperty("canonicalTitle")
    val canonicalTitle: String
)

/* Relationships */
data class KitsuSearchRelationshipsDto(
    @JsonProperty("mappings")
    val mappings: KitsuSearchRelationshipsMappingsDto
)

data class KitsuSearchRelationshipsMappingsDto(
    @JsonProperty("data")
    val data: List<KitsuSearchRelationshipsMappingDto>
)

data class KitsuSearchRelationshipsMappingDto(
    @JsonProperty("id")
    val id: String
)
