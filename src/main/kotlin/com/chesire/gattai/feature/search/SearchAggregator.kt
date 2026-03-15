package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.Series
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.provider.mapping.SeriesIdMappingEntry
import com.chesire.gattai.provider.mapping.SeriesIdMappingProvider
import org.springframework.stereotype.Service

@Service
class SearchAggregator(
    private val services: List<SearchService>,
    private val seriesIdMappingProvider: SeriesIdMappingProvider
) {

    @Suppress("CyclomaticComplexMethod")
    fun findSeries(params: SearchParams): List<Series> {
        val allResults = services.flatMap { it.search(params) }

        val idToIndex = mutableMapOf<String, Int>()
        val accumulated = mutableListOf<Pair<SeriesIdMappingEntry, MutableList<SearchModel>>>()

        for (result in allResults) {
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

        return accumulated.map { (entry, models) ->
            val isAnime = models.first().seriesType == SeriesType.ANIME
            val anyMissing = entry.kitsuId == null || entry.malId == null || entry.anilistId == null
            val resolvedEntry = if (isAnime && anyMissing) {
                seriesIdMappingProvider.findById(entry.kitsuId, entry.malId, entry.anilistId) ?: entry
            } else {
                entry
            }
            buildSeries(resolvedEntry, models)
        }
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
