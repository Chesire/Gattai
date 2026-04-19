package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchModel
import com.chesire.gattai.domain.search.SearchQuery
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.domain.search.SearchServiceResult
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchDataDto
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchIncludedDto
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KitsuSearchService(private val client: KitsuClient) : SearchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Suppress("TooGenericExceptionCaught")
    override fun search(query: SearchQuery): SearchServiceResult {
        return try {
            val result = client.executeGet<KitsuSearchResponseDto>(buildDestination(query))
            if (result.statusCode.is2xxSuccessful) {
                val data = result.toModels(query.seriesType)
                if (data.isNotEmpty()) {
                    SearchServiceResult.Success(data)
                } else {
                    SearchServiceResult.NoResults
                }
            } else {
                logger.error("Kitsu search failed with status code ${result.statusCode} and body: ${result.body}")
                SearchServiceResult.Error("Kitsu search failed with status code ${result.statusCode}")
            }
        } catch (ex: Exception) {
            logger.error("Kitsu search failed", ex)
            SearchServiceResult.Error("Kitsu search failed with exception")
        }
    }

    private fun buildDestination(query: SearchQuery): String {
        val basePath = when (query.seriesType) {
            SeriesType.ANIME -> ANIME_DESTINATION
            SeriesType.MANGA -> MANGA_DESTINATION
        }
        val (fieldsKey, fieldsValue) = when (query.seriesType) {
            SeriesType.ANIME -> ANIME_FIELDS_KEY to ANIME_FIELDS_VALUE
            SeriesType.MANGA -> MANGA_FIELDS_KEY to MANGA_FIELDS_VALUE
        }
        return UriComponentsBuilder.fromPath(basePath)
            .queryParam(QUERY_KEY, query.title)
            .queryParam(INCLUDE_KEY, INCLUDE_VALUE)
            .queryParam(LIMIT_KEY, LIMIT_VALUE)
            .queryParam(fieldsKey, fieldsValue)
            .build()
            .toUriString()
    }

    private fun ResponseEntity<KitsuSearchResponseDto>.toModels(seriesType: SeriesType): List<SearchModel> {
        return body
            ?.data
            ?.map { item ->
                val includedMap = body?.included?.associateBy { it.id }.orEmpty()
                val mappings = item.relationships?.mappings?.data
                    ?.mapNotNull { includedMap[it.id] }
                    .orEmpty()

                buildSearchModel(
                    seriesType = seriesType,
                    data = item,
                    mappings = mappings
                )
            }
            .orEmpty()
    }

    private fun buildSearchModel(
        seriesType: SeriesType,
        data: KitsuSearchDataDto,
        mappings: List<KitsuSearchIncludedDto>
    ): SearchModel {
        return SearchModel(
            ids = Ids(
                kitsuId = data.id,
                malId = mappings.firstOrNull {
                    it.attributes.externalSite.startsWith(MAPPING_MAL)
                }?.attributes?.externalId,
                anilistId = mappings.firstOrNull {
                    it.attributes.externalSite.startsWith(MAPPING_ANILIST)
                }?.attributes?.externalId
            ),
            title = data.attributes.canonicalTitle,
            seriesType = seriesType
        )
    }

    companion object {
        private const val ANIME_DESTINATION = "/anime"
        private const val MANGA_DESTINATION = "/manga"
        private const val QUERY_KEY = "filter[text]"
        private const val INCLUDE_KEY = "include"
        private const val INCLUDE_VALUE = "mappings"
        private const val LIMIT_KEY = "page[limit]"
        private const val LIMIT_VALUE = 20
        private const val ANIME_FIELDS_KEY = "fields[anime]"
        private const val ANIME_FIELDS_VALUE = "canonicalTitle,mappings"
        private const val MANGA_FIELDS_KEY = "fields[manga]"
        private const val MANGA_FIELDS_VALUE = "canonicalTitle,mappings"

        private const val MAPPING_MAL = "myanimelist/"
        private const val MAPPING_ANILIST = "anilist/"
    }
}
