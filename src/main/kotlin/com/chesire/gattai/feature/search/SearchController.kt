package com.chesire.gattai.feature.search

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController {

    @GetMapping
    fun findSeries(@RequestParam params: SearchParams) {
        // talk to aggregator to find matching series
    }
}