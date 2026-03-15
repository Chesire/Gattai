package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchDataDto
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchDto
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchIncludedDto
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KitsuSearchService(private val client: KitsuClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        if (params.isEmpty) {
            // TODO: Throw exception and handle as error?
            return emptyList()
        }

        return client.executeGet<KitsuSearchDto>(buildDestination(params))
            .body
            ?.let { dto ->
                val includedMap = dto.included?.associateBy { it.id }.orEmpty()
                dto.data.map { item ->
                    val mappings = item.relationships?.mappings?.data
                        ?.mapNotNull { includedMap[it.id] }
                        .orEmpty()

                    buildSearchModel(
                        seriesType = params.seriesType,
                        data = item,
                        mappings = mappings
                    )
                }
            }
            .orEmpty() // TODO: Handle better
    }

    private fun buildDestination(params: SearchParams): String {
        val basePath = when (params.seriesType) {
            SeriesType.ANIME -> ANIME_DESTINATION
            SeriesType.MANGA -> MANGA_DESTINATION
        }
        val (fieldsKey, fieldsValue) = when (params.seriesType) {
            SeriesType.ANIME -> ANIME_FIELDS_KEY to ANIME_FIELDS_VALUE
            SeriesType.MANGA -> MANGA_FIELDS_KEY to MANGA_FIELDS_VALUE
        }
        return UriComponentsBuilder.fromPath(basePath)
            .queryParam(QUERY_KEY, params.title)
            .queryParam(INCLUDE_KEY, INCLUDE_VALUE)
            .queryParam(LIMIT_KEY, LIMIT_VALUE)
            .queryParam(fieldsKey, fieldsValue)
            .build()
            .toUriString()
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
