package com.chesire.gattai.provider.mal

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.provider.mal.dto.MalSearchDto
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class MalSearchService(private val client: MalClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        return client.executeGet<MalSearchDto>(buildDestination(params))
            .body
            ?.let { dto ->
                dto.data.map { item ->
                    SearchModel(
                        ids = Ids(malId = item.node.id.toString()),
                        title = item.node.title,
                        seriesType = params.seriesType
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
        return UriComponentsBuilder.fromPath(basePath)
            .queryParam(QUERY_KEY, params.title)
            .queryParam(LIMIT_KEY, LIMIT_VALUE)
            .queryParam(FIELDS_KEY, FIELDS_VALUE)
            .build()
            .toUriString()
    }

    companion object {
        private const val ANIME_DESTINATION = "/anime"
        private const val MANGA_DESTINATION = "/manga"
        private const val QUERY_KEY = "q"
        private const val LIMIT_KEY = "limit"
        private const val LIMIT_VALUE = 20
        private const val FIELDS_KEY = "fields"
        private const val FIELDS_VALUE = "id,title"
    }
}
