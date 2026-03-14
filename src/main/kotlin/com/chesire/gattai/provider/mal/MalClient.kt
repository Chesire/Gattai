package com.chesire.gattai.provider.mal

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MalClient(@Value($$"${mal.client-id}") private val clientId: String) {

    @PublishedApi
    internal val restClient = RestClient.builder()
        .baseUrl(BASE_URL)
        .defaultHeaders { headers ->
            headers.add("X-MAL-CLIENT-ID", clientId)
        }
        .build()

    final inline fun <reified T : Any> executeGet(destination: String): ResponseEntity<T> {
        return try {
            restClient
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
        private const val BASE_URL = "https://api.myanimelist.net/v2/"
    }
}
