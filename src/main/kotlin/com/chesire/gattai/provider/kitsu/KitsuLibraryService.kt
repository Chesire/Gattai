package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.Tokens
import com.chesire.gattai.domain.retrievelibrary.LibraryModel
import com.chesire.gattai.domain.retrievelibrary.RetrieveLibraryQuery
import com.chesire.gattai.domain.retrievelibrary.RetrieveLibraryService
import com.chesire.gattai.domain.retrievelibrary.RetrieveServiceResult
import com.chesire.gattai.provider.kitsu.dto.KitsuLibraryDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KitsuLibraryService(
    private val client: KitsuClient,
    private val userService: KitsuUserService
) : RetrieveLibraryService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun extractToken(tokens: Tokens): String? = tokens.kitsuToken

    override fun retrieveLibrary(query: RetrieveLibraryQuery): RetrieveServiceResult {
        val userId = userService.retrieveUserId(query.token)
        if (userId.isNullOrBlank()) {
            return RetrieveServiceResult.Error("Failed to retrieve user id")
        }

        return try {
            val result = client.executeGet<KitsuLibraryDto>(buildDestination(query, userId))
            if (result.statusCode.is2xxSuccessful) {
                val data = result.toModels(query.seriesType)
                if (data.isNotEmpty()) {
                    RetrieveServiceResult.Success(data)
                } else {
                    RetrieveServiceResult.NoResults
                }
            } else {
                logger.error("Kitsu retrieve library failed with status code ${result.statusCode} and body: ${result.body}")
                RetrieveServiceResult.Error("Kitsu retrieve library failed with status code ${result.statusCode}")
            }
        } catch (ex: Exception) {
            logger.error("Kitsu retrieve library failed", ex)
            RetrieveServiceResult.Error("Kitsu retrieve library failed with exception")
        }
    }

    private fun buildDestination(query: RetrieveLibraryQuery, userId: String): String {
        val typeString = query.seriesType.toString().lowercase()
        val (fieldsKey, fieldsValue) = when (query.seriesType) {
            SeriesType.ANIME -> ANIME_FIELDS_KEY to ANIME_FIELDS_VALUE
            SeriesType.MANGA -> MANGA_FIELDS_KEY to MANGA_FIELDS_VALUE
        }
        return UriComponentsBuilder.fromPath(DESTINATION)
            .queryParam(INCLUDE_KEY, "${typeString},${typeString}.$INCLUDE_VALUE_SUFFIX")
            .queryParam(KIND_KEY, typeString)
            .queryParam(USERID_KEY, userId)
            .queryParam(LIMIT_KEY, LIMIT_VALUE)
            .queryParam(fieldsKey, fieldsValue)
            .build()
            .toUriString()
    }

    private fun ResponseEntity<KitsuLibraryDto>.toModels(seriesType: SeriesType): List<LibraryModel> {
        // TODO: Populate
        return emptyList()
    }

    companion object {
        private const val DESTINATION = "/library-entries"

        private const val INCLUDE_KEY = "include"
        private const val INCLUDE_VALUE_SUFFIX = "mappings"
        private const val LIMIT_KEY = "page[limit]"
        private const val LIMIT_VALUE = 20
        private const val KIND_KEY = "filter[kind]"
        private const val USERID_KEY = "filter[userId]"
        private const val ANIME_FIELDS_KEY = "fields[anime]"
        private const val ANIME_FIELDS_VALUE = "canonicalTitle,mappings"
        private const val MANGA_FIELDS_KEY = "fields[manga]"
        private const val MANGA_FIELDS_VALUE = "canonicalTitle,mappings"

        private const val MAPPING_MAL = "myanimelist/"
        private const val MAPPING_ANILIST = "anilist/"
    }
}
