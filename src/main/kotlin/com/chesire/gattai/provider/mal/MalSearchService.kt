package com.chesire.gattai.provider.mal

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchModel
import com.chesire.gattai.domain.search.SearchQuery
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.domain.search.SearchServiceResult
import com.chesire.gattai.provider.mal.dto.MalSearchDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class MalSearchService(private val client: MalClient) : SearchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun search(query: SearchQuery): SearchServiceResult {
        return try {
            val result = client.executeGet<MalSearchDto>(buildDestination(query))
            if (result.statusCode.is2xxSuccessful) {
                val data = result.toModels(query.seriesType)
                if (data.isNotEmpty()) {
                    SearchServiceResult.Success(data)
                } else {
                    SearchServiceResult.NoResults
                }
            } else {
                logger.error("Mal search failed with status code ${result.statusCode} and body: ${result.body}")
                SearchServiceResult.Error("Mal search failed with status code ${result.statusCode}")
            }
        } catch (ex: Exception) {
            logger.error("Mal search failed", ex)
            SearchServiceResult.Error("Mal search failed with exception")
        }
    }

    private fun buildDestination(query: SearchQuery): String {
        val basePath = when (query.seriesType) {
            SeriesType.ANIME -> ANIME_DESTINATION
            SeriesType.MANGA -> MANGA_DESTINATION
        }
        return UriComponentsBuilder.fromPath(basePath)
            .queryParam(QUERY_KEY, query.title)
            .queryParam(LIMIT_KEY, LIMIT_VALUE)
            .queryParam(FIELDS_KEY, FIELDS_VALUE)
            .build()
            .toUriString()
    }

    private fun ResponseEntity<MalSearchDto>.toModels(seriesType: SeriesType): List<SearchModel> {
        return body
            ?.data
            ?.map { item ->
                SearchModel(
                    ids = Ids(malId = item.node.id.toString()),
                    title = item.node.title,
                    seriesType = seriesType
                )
            }
            .orEmpty()
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
