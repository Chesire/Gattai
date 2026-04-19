package com.chesire.gattai.provider.kitsu.dto

import com.fasterxml.jackson.annotation.JsonProperty

internal data class KitsuLibraryResponseDto(
    @JsonProperty("data")
    val data: List<KitsuLibraryDataDto>,
    @JsonProperty("included")
    val included: List<KitsuLibraryIncludedDto>?
)

/* Data */
internal data class KitsuLibraryDataDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("relationships")
    val relationships: KitsuLibraryRelationshipsDto?
)

internal data class KitsuLibraryRelationshipsDto(
    @JsonProperty("anime")
    val anime: KitsuLibraryRelationshipEntryDto?,
    @JsonProperty("manga")
    val manga: KitsuLibraryRelationshipEntryDto?
)

internal data class KitsuLibraryRelationshipEntryDto(
    @JsonProperty("data")
    val data: KitsuLibraryRelationshipDataDto?
)

internal data class KitsuLibraryRelationshipDataDto(
    @JsonProperty("id")
    val id: String
)

/* Included */
internal data class KitsuLibraryIncludedDto(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("attributes")
    val attributes: KitsuLibraryIncludedAttributesDto
)

/**
 * Nullable fields as this can represent two different attributes objects, parsed on the parent included dto.
 */
internal data class KitsuLibraryIncludedAttributesDto(
    @JsonProperty("canonicalTitle")
    val canonicalTitle: String?,
    @JsonProperty("externalSite")
    val externalSite: String?,
    @JsonProperty("externalId")
    val externalId: String?
)
