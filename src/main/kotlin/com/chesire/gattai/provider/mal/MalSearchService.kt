package com.chesire.gattai.provider.mal

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.provider.mal.dto.MalSearchDto
import org.springframework.stereotype.Component

@Component
class MalSearchService(private val client: MalClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        if (params.isEmpty) {
            // TODO: Throw exception and handle as error?
            return emptyList()
        }

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
        return buildString {
            when (params.seriesType) {
                SeriesType.MANGA -> append(MANGA_DESTINATION)
                else -> append(ANIME_DESTINATION)
            }
            append("?")
            append("q=${params.title}")
            append("&")
            append(LIMIT)
            append("&")
            append(FIELDS)
        }
    }

    companion object {
        private const val ANIME_DESTINATION = "/anime"
        private const val MANGA_DESTINATION = "/manga"
        private const val LIMIT = "limit=20"
        private const val FIELDS = "fields=id,title"
    }
}
