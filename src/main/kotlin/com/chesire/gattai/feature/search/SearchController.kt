package com.chesire.gattai.feature.search

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(private val aggregator: SearchAggregator) {

    @GetMapping
    fun findSeries(@RequestParam params: SearchParams) {
        val series = aggregator.findSeries(params)
    }
}