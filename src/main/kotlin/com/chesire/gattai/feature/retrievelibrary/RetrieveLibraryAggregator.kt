package com.chesire.gattai.feature.retrievelibrary

import com.chesire.gattai.domain.SeriesEntry
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.Tokens
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
        // Do calls to services and mapping etc
        return AggregatedRetrieveResult.NoResults
    }
}

sealed interface AggregatedRetrieveResult {
    data class Success(val series: List<SeriesEntry>) : AggregatedRetrieveResult
    data object NoResults : AggregatedRetrieveResult
    data class Error(val message: String) : AggregatedRetrieveResult
}
