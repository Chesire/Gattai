package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.SeriesEntry
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/retrieve-library")
class RetrieveLibraryController {

    @GetMapping
    fun retrieveLibrary(
        @RequestHeader("kitsu-token") kitsuToken: String,
        @RequestHeader("anilist-token") anilistToken: String,
        @RequestHeader("mal-token") malToken: String,
    ): ResponseEntity<Any> {
        // Send off to a service
        return ResponseEntity
            .ok()
            .body(listOf<SeriesEntry>())
    }
}
