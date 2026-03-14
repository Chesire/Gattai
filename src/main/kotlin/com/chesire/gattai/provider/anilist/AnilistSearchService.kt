package com.chesire.gattai.provider.anilist

import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import org.springframework.stereotype.Component

@Component
class AnilistSearchService(private val anilistClient: AnilistClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        // call client
        return emptyList()
    }
}
