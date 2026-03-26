package com.chesire.gattai.provider.anilist

import com.chesire.gattai.provider.anilist.dto.AnilistQueryDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class AnilistClient(@Value($$"${anilist.base-url}") private val baseUrl: String) {

    @PublishedApi
    internal val restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .build()

    final inline fun <reified T : Any> executeRequest(query: AnilistQueryDto): ResponseEntity<T> {
        return restClient
            .post()
            .body(query)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<T>() {})
    }
}
