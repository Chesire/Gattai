package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.SeriesEntry
import com.chesire.gattai.error.ErrorResponse
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
        if (!hasValidToken(kitsuToken = kitsuToken, anilistToken = anilistToken, malToken = malToken)) {
            return ResponseEntity
                .badRequest()
                .body(
                    ErrorResponse("No tokens provided")
                )
        }

        // Send off to a service
        return ResponseEntity
            .ok()
            .body(listOf<SeriesEntry>())
    }

    private fun hasValidToken(kitsuToken: String?, anilistToken: String?, malToken: String?): Boolean {
        return kitsuToken != null || anilistToken != null || malToken != null
    }
}
