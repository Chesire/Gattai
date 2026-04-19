package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.SeriesEntry
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.Tokens
import com.chesire.gattai.domain.retrievelibrary.RetrieveLibraryQuery
import com.chesire.gattai.domain.retrievelibrary.RetrieveLibraryService
import com.chesire.gattai.provider.mapping.SeriesIdMappingProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RetrieveLibraryAggregator(
    private val services: List<RetrieveLibraryService>,
    private val seriesIdMappingProvider: SeriesIdMappingProvider
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun retrieveLibraries(tokens: Tokens, seriesType: SeriesType): AggregatedRetrieveResult {
        val allResults = services.mapNotNull { service ->
            val token = service.extractToken(tokens)
            if (token != null) {
                service.retrieveLibrary(RetrieveLibraryQuery(token, seriesType))
            } else {
                null
            }
        }

        // NEXT STEPS:
        // Implement the services
        // Implement the mapping in here
        return AggregatedRetrieveResult.NoResults
    }
}

sealed interface AggregatedRetrieveResult {
    data class Success(val series: List<SeriesEntry>, val warnings: List<String>) : AggregatedRetrieveResult
    data object NoResults : AggregatedRetrieveResult
    data class Error(val message: String) : AggregatedRetrieveResult
}
