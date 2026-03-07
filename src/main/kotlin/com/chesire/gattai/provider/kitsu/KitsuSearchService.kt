package com.chesire.gattai.provider.kitsu

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchParams
import org.springframework.stereotype.Component

@Component
class KitsuSearchService(private val client: KitsuClient) : SearchService {
    override fun search(params: SearchParams): List<Series> {
        // call client
        return emptyList()
    }
}