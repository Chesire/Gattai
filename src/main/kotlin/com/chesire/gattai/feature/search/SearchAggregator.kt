package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchModel
import com.chesire.gattai.domain.search.SearchQuery
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.domain.search.SearchServiceResult
import com.chesire.gattai.provider.mapping.SeriesIdMappingEntry
import com.chesire.gattai.provider.mapping.SeriesIdMappingProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SearchAggregator(
    private val services: List<SearchService>,
    private val seriesIdMappingProvider: SeriesIdMappingProvider
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Suppress("CyclomaticComplexMethod")
    fun findSeries(query: SearchQuery): AggregatedSearchResult {
        val allResults = services.map { it.search(query) }
        if (allResults.all { it is SearchServiceResult.Error }) {
            logger.error("All search services threw an error")
            return AggregatedSearchResult.Error("All search services failed")
        } else if (allResults.all { it is SearchServiceResult.NoResults }) {
            logger.warn("All search services returned no results")
            return AggregatedSearchResult.NoResults
        }

        allResults.filterIsInstance<SearchServiceResult.Error>().let {
            if (it.isNotEmpty()) {
                logger.info("${it.count()} search service calls have failed, some still succeeded")
            }
        }
        val results = allResults.filterIsInstance<SearchServiceResult.Success>().flatMap { it.searchModels }

        val idToIndex = mutableMapOf<String, Int>()
        val accumulated = mutableListOf<Pair<SeriesIdMappingEntry, MutableList<SearchModel>>>()

        for (result in results) {
            val ids = result.ids
            val existingIndex = ids.kitsuId?.let { idToIndex[it] }
                ?: ids.malId?.let { idToIndex[it] }
                ?: ids.anilistId?.let { idToIndex[it] }

            if (existingIndex != null) {
                val (existingEntry, models) = accumulated[existingIndex]
                val mergedEntry = existingEntry.copy(
                    kitsuId = existingEntry.kitsuId ?: ids.kitsuId,
                    malId = existingEntry.malId ?: ids.malId,
                    anilistId = existingEntry.anilistId ?: ids.anilistId
                )
                accumulated[existingIndex] = mergedEntry to models.also { it.add(result) }
                ids.kitsuId?.let { idToIndex.putIfAbsent(it, existingIndex) }
                ids.malId?.let { idToIndex.putIfAbsent(it, existingIndex) }
                ids.anilistId?.let { idToIndex.putIfAbsent(it, existingIndex) }
            } else {
                val newIndex = accumulated.size
                accumulated.add(SeriesIdMappingEntry(ids.kitsuId, ids.malId, ids.anilistId) to mutableListOf(result))
                ids.kitsuId?.let { idToIndex[it] = newIndex }
                ids.malId?.let { idToIndex[it] = newIndex }
                ids.anilistId?.let { idToIndex[it] = newIndex }
            }
        }

        val mappedModels = accumulated.map { (entry, models) ->
            val isAnime = models.first().seriesType == SeriesType.ANIME
            val anyMissing = entry.kitsuId == null || entry.malId == null || entry.anilistId == null
            val resolvedEntry = if (isAnime && anyMissing) {
                seriesIdMappingProvider.findById(entry.kitsuId, entry.malId, entry.anilistId) ?: entry
            } else {
                entry
            }
            buildSeries(resolvedEntry, models)
        }

        return AggregatedSearchResult.Success(mappedModels)
    }

    private fun buildSeries(mapping: SeriesIdMappingEntry, seriesList: List<SearchModel>): Series {
        return Series(
            kitsuId = mapping.kitsuId,
            malId = mapping.malId,
            aniListId = mapping.anilistId,
            titles = seriesList.map { it.title }.distinct(),
            seriesType = seriesList.first().seriesType,
        )
    }
}

sealed interface AggregatedSearchResult {
    data class Success(val series: List<Series>) : AggregatedSearchResult
    data object NoResults : AggregatedSearchResult
    data class Error(val message: String) : AggregatedSearchResult
}
