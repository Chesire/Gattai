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
        return when (model.targetType) {
            TargetType.KITSU -> seriesIdMappingProvider.findById(model.id, null, null)
            TargetType.MAL -> seriesIdMappingProvider.findById(null, model.id, null)
            TargetType.ANILIST -> seriesIdMappingProvider.findById(null, null, model.id)
        }
    }

    private fun buildSeries(mapping: SeriesIdMappingEntry, seriesList: List<SearchModel>): Series {
        return Series(
            kitsuId = mapping.kitsuId,
            malId = mapping.malId,
            aniListId = mapping.anilistId,
            title = seriesList.firstOrNull { it.title.isNotBlank() }?.title ?: "",
            seriesType = SeriesType.ANIME // TODO: Handle Manga as well
        )
    }
}
