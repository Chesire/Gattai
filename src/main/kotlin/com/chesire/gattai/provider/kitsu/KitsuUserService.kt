package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.provider.kitsu.dto.KitsuUserResponseDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KitsuUserService(private val client: KitsuClient) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun retrieveUserId(token: String): String? {
        return try {
            val result = client.executeGet<KitsuUserResponseDto>(DESTINATION, token)
            if (result.statusCode.is2xxSuccessful) {
                val body = result.body?.data?.firstOrNull()
                if (body == null) {
                    logger.error("Kitsu retrieve user id failed, empty body")
                    null
                } else {
                    logger.debug("Kitsu retrieve user id successful")
                    body.id
                }
            } else {
                logger.error(
                    "Kitsu retrieve user id failed with status code ${result.statusCode} and body: ${result.body}"
                )
                null
            }
        } catch (ex: Exception) {
            logger.error("Kitsu retrieve user id failed", ex)
            null
        }
    }

    companion object {
        private val DESTINATION = UriComponentsBuilder.fromPath("/users")
            .queryParam(SELF_KEY, true)
            .build()
            .toUriString()
        private const val SELF_KEY = "filter[self]"
    }
}
