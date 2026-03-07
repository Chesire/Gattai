package com.chesire.gattai.provider.mal

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.feature.search.SearchParams
import org.springframework.stereotype.Component

@Component
class MalSearchService(private val malClient: MalClient) : SearchService {
    override fun search(params: SearchParams): List<Series> {
        TODO("Not yet implemented")
    }
}
