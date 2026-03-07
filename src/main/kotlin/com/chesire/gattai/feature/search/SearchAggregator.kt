package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.search.SearchService
import org.springframework.stereotype.Service

@Service
class SearchAggregator(private val services: List<SearchService>) {

    fun findSeries(params: SearchParams): List<Series> {
        return services.flatMap { it.search(params) }
        // do parsing on the results
    }
}
