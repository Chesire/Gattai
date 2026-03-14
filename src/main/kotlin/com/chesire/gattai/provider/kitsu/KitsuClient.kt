package com.chesire.gattai.provider.kitsu

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class KitsuClient {

    @PublishedApi
    internal val kitsuRestClient = RestClient.builder()
        .baseUrl(BASE_URL)
        .defaultHeaders { headers ->
            headers.accept = listOf(MEDIA_TYPE)
            headers.contentType = MEDIA_TYPE
        }
        .build()

    final inline fun <reified T : Any> executeGet(destination: String): ResponseEntity<T> {
        return try {
            kitsuRestClient
                .get()
                .uri(destination)
                .retrieve()
                .toEntity(object : ParameterizedTypeReference<T>() {})
        } catch (ex: Exception) {
            // TODO: Handle ex properly
            ResponseEntity.internalServerError().build()
        }
    }

    companion object {
        private const val BASE_URL = "https://kitsu.io/api/edge"
        private val MEDIA_TYPE = MediaType("application", "vnd.api+json")
    }
}
