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
                val includedMap = dto.included?.associateBy { it.id } ?: emptyMap()
                dto.data.map { item ->
                    val mappings = item.relationships?.mappings?.data
                        ?.mapNotNull { includedMap[it.id] }
                        ?: emptyList()

                    buildSearchModel(
                        seriesType = params.seriesType,
                        data = item,
                        mappings = mappings
                    )
                }
            }
            ?: emptyList() // TODO: Handle error better
    }

    private fun buildDestination(params: SearchParams): String {
        return buildString {
            when (params.seriesType) {
                SeriesType.MANGA -> append(MANGA_DESTINATION)
                else -> append(ANIME_DESTINATION)
            }
            append("?")
            append("filter[text]=${params.title}")
            append("&")
            append(INCLUDE)
            append("&")
            append(LIMIT)
            append("&")
            when (params.seriesType) {
                SeriesType.MANGA -> append(MANGA_FIELDS)
                else -> append(ANIME_FIELDS)
            }
        }
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
        private const val INCLUDE = "include=mappings"
        private const val ANIME_FIELDS = "fields[anime]=canonicalTitle,mappings"
        private const val MANGA_FIELDS = "fields[manga]=canonicalTitle,mappings"
        private const val LIMIT = "page[limit]=20"

        private const val MAPPING_MAL = "myanimelist/"
        private const val MAPPING_ANILIST = "anilist/"
    }
}
