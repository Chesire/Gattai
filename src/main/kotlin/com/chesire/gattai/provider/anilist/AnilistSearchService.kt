package com.chesire.gattai.provider.anilist

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchParams

class AnilistSearchService(private val anilistClient: AnilistClient) : SearchService {
    override fun search(params: SearchParams): List<Series> {
        TODO("Not yet implemented")
    }
}