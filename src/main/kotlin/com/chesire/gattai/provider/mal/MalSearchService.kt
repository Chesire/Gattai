package com.chesire.gattai.provider.mal

import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams
import org.springframework.stereotype.Component

@Component
class MalSearchService(private val malClient: MalClient) : SearchService {
    override fun search(params: SearchParams): List<SearchModel> {
        // call client
        return emptyList()
    }
}
