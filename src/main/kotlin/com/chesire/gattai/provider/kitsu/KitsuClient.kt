package com.chesire.gattai.provider.kitsu

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class KitsuClient(@Value($$"${kitsu.base-url}") private val baseUrl: String) {

    @PublishedApi
    internal val restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeaders { headers ->
            headers.accept = listOf(MEDIA_TYPE)
            headers.contentType = MEDIA_TYPE
        }
        .build()

    final inline fun <reified T : Any> executeGet(destination: String): ResponseEntity<T> {
        return restClient
            .get()
            .uri(destination)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<T>() {})
    }

    companion object {
        private val MEDIA_TYPE = MediaType("application", "vnd.api+json")
    }
}
