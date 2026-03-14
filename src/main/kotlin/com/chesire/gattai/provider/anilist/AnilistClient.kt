package com.chesire.gattai.provider.anilist

import com.chesire.gattai.provider.anilist.dto.AnilistQueryDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class AnilistClient {

    @PublishedApi
    internal val restClient = RestClient.builder()
        .baseUrl(BASE_URL)
        .build()

    final inline fun <reified T : Any> executeRequest(query: AnilistQueryDto): ResponseEntity<T> {
        return try {
            restClient
                .post()
                .body(query)
                .retrieve()
                .toEntity(object : ParameterizedTypeReference<T>() {})
        } catch (ex: Exception) {
            // TODO: Handle ex properly
            ResponseEntity.internalServerError().build()
        }
    }

    companion object {
        private const val BASE_URL = "https://graphql.anilist.co"
    }
}
