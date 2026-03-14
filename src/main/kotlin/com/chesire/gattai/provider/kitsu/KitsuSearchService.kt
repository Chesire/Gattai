package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.feature.search.TargetType
import com.chesire.gattai.provider.kitsu.dto.KitsuSearchDto
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
                dto.data.map {
                    SearchModel(
                        id = it.id,
                        targetType = TargetType.KITSU,
                        title = it.attributes.canonicalTitle,
                        seriesType = SeriesType.ANIME
                    )
                }
            } ?: emptyList() // TODO: Handle error better
    }

    private fun buildDestination(params: SearchParams): String {
        return buildString {
            append(ANIME_DESTINATION)
            append("?")
            append(FIELDS)
            append("&")
            if (params.title.isNotBlank()) {
                append("filter[text]=${params.title}")
            }
        }
    }

    companion object {
        private const val ANIME_DESTINATION = "/anime"
        private const val FIELDS = "fields[anime]=canonicalTitle"
    }
}
