package com.chesire.gattai.provider.anilist

import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.Ids
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import com.chesire.gattai.provider.anilist.dto.AnilistQueryDto
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
            ?: emptyList() // TODO: Handle better
    }

    private fun buildQuery(params: SearchParams): AnilistQueryDto {
        return AnilistQueryDto(
            """
                query {
                  Page(perPage: 20) {
                    media(search: "${params.title}", type: ${params.seriesType}) {
                      id
                      title { romaji }
                      idMal
                    }
                  }
                }
              """.trimIndent()
        )
    }
}
