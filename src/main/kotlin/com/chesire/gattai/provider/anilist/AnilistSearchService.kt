package com.chesire.gattai.provider.anilist

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchModel
import com.chesire.gattai.domain.search.SearchQuery
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.domain.search.SearchServiceResult
import com.chesire.gattai.provider.anilist.dto.AnilistQueryDto
import com.chesire.gattai.provider.anilist.dto.AnilistQueryVariablesDto
import com.chesire.gattai.provider.anilist.dto.AnilistSearchDto
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AnilistSearchService(private val client: AnilistClient) : SearchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun search(query: SearchQuery): SearchServiceResult {
        return try {
            val result = client.executeRequest<AnilistSearchDto>(buildQuery(query))
            if (result.statusCode.is2xxSuccessful) {
                val data = result.toModels(query.seriesType)
                if (data.isNotEmpty()) {
                    SearchServiceResult.Success(data)
                } else {
                    SearchServiceResult.NoResults
                }
            } else {
                logger.error("Anilist search failed with status code ${result.statusCode} and body: ${result.body}")
                SearchServiceResult.Error("Anilist search failed with status code ${result.statusCode}")
            }
        } catch (ex: Exception) {
            logger.error("Anilist search failed", ex)
            SearchServiceResult.Error("Anilist search failed with exception")
        }
    }

    private fun buildQuery(query: SearchQuery) = AnilistQueryDto(
        query = SEARCH_QUERY,
        variables = AnilistQueryVariablesDto(
            search = query.title,
            type = query.seriesType.toString()
        )
    )

    private fun ResponseEntity<AnilistSearchDto>.toModels(seriesType: SeriesType): List<SearchModel> {
        return body
            ?.data
            ?.page
            ?.media
            ?.map { item ->
                SearchModel(
                    ids = Ids(
                        malId = item.idMal?.toString(),
                        anilistId = item.id.toString()
                    ),
                    title = item.title.romaji,
                    seriesType = seriesType
                )
            }
            .orEmpty()
    }

    companion object {
        private val SEARCH_QUERY = $$"""
            query ($search: String, $type: MediaType) {
              Page(perPage: 20) {
                media(search: $search, type: $type) {
                  id
                  title { romaji }
                  idMal
                }
              }
            }
        """.trimIndent()
    }
}
