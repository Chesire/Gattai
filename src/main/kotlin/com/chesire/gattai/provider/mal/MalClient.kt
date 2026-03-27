package com.chesire.gattai.provider.mal

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MalClient(
    @Value($$"${mal.base-url}") private val baseUrl: String,
    @Value($$"${mal.client-id}") private val clientId: String,
) {

    @PublishedApi
    internal val restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeaders { headers ->
            headers.add("X-MAL-CLIENT-ID", clientId)
        }
        .build()

    final inline fun <reified T : Any> executeGet(destination: String): ResponseEntity<T> {
        return restClient
            .get()
            .uri(destination)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<T>() {})
    }
}
