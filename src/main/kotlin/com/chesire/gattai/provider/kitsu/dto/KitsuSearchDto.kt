package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KitsuSearchDto(
    @JsonProperty("data")
    val data: List<KitsuSearchDataDto>,
    @JsonProperty("included")
    val included: List<KitsuSearchIncludedDto>?
)

data class KitsuSearchDataDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("type")
    val type: String, // TODO: Use enum
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

/* Included */
data class KitsuSearchIncludedDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("attributes")
    val attributes: KitsuSearchIncludedAttributesDto
)

data class KitsuSearchIncludedAttributesDto(
    @JsonProperty("externalSite")
    val externalSite: String, // TODO: Use an enum
    @JsonProperty("externalId")
    val externalId: String
)
