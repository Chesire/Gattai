package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.search.SearchQuery
import com.chesire.gattai.error.ErrorResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(private val aggregator: SearchAggregator) {

    @GetMapping
    fun findSeries(@Valid @ModelAttribute params: SearchParams): ResponseEntity<Any> {
        return when (val searchResult = aggregator.findSeries(params.toQuery())) {
            is AggregatedSearchResult.Success -> ResponseEntity.ok(searchResult.series)
            AggregatedSearchResult.NoResults -> ResponseEntity.noContent().build()
            is AggregatedSearchResult.Error -> ResponseEntity
                .internalServerError()
                .body(ErrorResponse(message = "Search failed"))
        }
    }

    private fun SearchParams.toQuery(): SearchQuery {
        return SearchQuery(
            title = title,
            seriesType = seriesType
        )
    }
}
