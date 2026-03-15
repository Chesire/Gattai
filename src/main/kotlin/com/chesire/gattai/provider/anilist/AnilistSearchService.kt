package com.chesire.gattai.provider.anilist

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.provider.anilist.dto.AnilistQueryDto
import com.chesire.gattai.provider.anilist.dto.AnilistQueryVariablesDto
import com.chesire.gattai.provider.anilist.dto.AnilistSearchDto
import org.springframework.stereotype.Component

@Component
class AnilistSearchService(private val client: AnilistClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        if (params.isEmpty) {
            // TODO: Throw exception and handle as error?
            return emptyList()
        }

        return client.executeRequest<AnilistSearchDto>(buildQuery(params))
            .body
            ?.let { dto ->
                dto.data.page.media.map { item ->
                    SearchModel(
                        ids = Ids(
                            malId = item.idMal?.toString(),
                            anilistId = item.id.toString()
                        ),
                        title = item.title.romaji,
                        seriesType = params.seriesType
                    )
                }
            }
            .orEmpty() // TODO: Handle better
    }

    private fun buildQuery(params: SearchParams) = AnilistQueryDto(
        query = SEARCH_QUERY,
        variables = AnilistQueryVariablesDto(
            search = params.title,
            type = params.seriesType.toString()
        )
    )

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
