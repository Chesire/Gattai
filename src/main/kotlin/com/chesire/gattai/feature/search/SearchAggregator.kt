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

    fun findSeries(params: SearchParams): List<Series> {
        val result = services
            .flatMap { it.search(params) }
            .groupBy(::getMapping)
            .mapNotNull { (mapping, seriesList) ->
                if (mapping == null) {
                    return@mapNotNull null
                }
                buildSeries(mapping, seriesList)
            }

        return result
    }

    private fun getMapping(model: SearchModel): SeriesIdMappingEntry? {
        if (!model.ids.kitsuId.isNullOrBlank() &&
            !model.ids.malId.isNullOrBlank() &&
            !model.ids.anilistId.isNullOrBlank()
        ) {
            // We have all ids, we can skip the provider
            return SeriesIdMappingEntry(
                kitsuId = model.ids.kitsuId,
                malId = model.ids.malId,
                anilistId = model.ids.anilistId
            )
        }

        if (model.seriesType != SeriesType.ANIME) {
            // We can only perform static mapping on anime
            return null
        }

        return seriesIdMappingProvider.findById(
            kitsuId = model.ids.kitsuId,
            malId = model.ids.malId,
            anilistId = model.ids.anilistId
        )
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
