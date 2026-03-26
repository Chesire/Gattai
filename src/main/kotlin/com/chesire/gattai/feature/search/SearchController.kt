package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.Series
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
    fun findSeries(@Valid @ModelAttribute params: SearchParams): ResponseEntity<List<Series>> {
        val series = aggregator.findSeries(params)
        return ResponseEntity.ok(series)
    }
}
