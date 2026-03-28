package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.Tokens
import com.chesire.gattai.error.ErrorResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/retrieve-library")
class RetrieveLibraryController(private val aggregator: RetrieveLibraryAggregator) {

    @GetMapping
    fun retrieveLibrary(
        @RequestHeader(value = "kitsu-token", required = false) kitsuToken: String?,
        @RequestHeader(value = "anilist-token", required = false) anilistToken: String?,
        @RequestHeader(value = "mal-token", required = false) malToken: String?,
        @Valid @ModelAttribute params: RetrieveLibraryParams
    ): ResponseEntity<Any> {
        val tokens = Tokens(
            kitsuToken = kitsuToken,
            anilistToken = anilistToken,
            malToken = malToken
        )
        if (!tokens.hasValidToken) {
            return ResponseEntity
                .badRequest()
                .body(ErrorResponse("No tokens provided"))
        }

        return when (val retrieveResult = aggregator.retrieveLibraries(tokens, params.seriesType)) {
            is AggregatedRetrieveResult.Success -> ResponseEntity.ok(retrieveResult.series)
            AggregatedRetrieveResult.NoResults -> ResponseEntity.noContent().build()
            is AggregatedRetrieveResult.Error ->
                ResponseEntity
                    .internalServerError()
                    .body(ErrorResponse(message = "Retrieve library failed"))
        }
    }
}
