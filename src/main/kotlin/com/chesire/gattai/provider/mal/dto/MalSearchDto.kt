package com.chesire.gattai.provider.mal.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MalSearchDto(
    @JsonProperty("data")
    val data: List<MalSearchDataDto>
)

data class MalSearchDataDto(
    @JsonProperty("node")
    val node: MalSearchNodeDto
)

data class MalSearchNodeDto(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("main_picture")
    val mainPicture: MalSearchMainPictureDto
)

data class MalSearchMainPictureDto(
    @JsonProperty("medium")
    val medium: String,
    @JsonProperty("large")
    val large: String
)
