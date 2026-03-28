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
        @RequestHeader(value = "kitsu-token", required = false) kitsuToken: String?,
        @RequestHeader(value = "anilist-token", required = false) anilistToken: String?,
        @RequestHeader(value = "mal-token", required = false) malToken: String?
    ): ResponseEntity<Any> {
        // Send off to a service
        return ResponseEntity
            .ok()
            .body(listOf<SeriesEntry>())
    }
}
