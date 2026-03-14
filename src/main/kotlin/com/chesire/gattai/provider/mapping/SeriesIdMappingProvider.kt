package com.chesire.gattai.provider.mapping

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class SeriesIdMappingProvider {

    private val _byKitsuId: Map<Int, SeriesIdMappingEntry>
    private val _byMalId: Map<Int, SeriesIdMappingEntry>
    private val _byAnilistId: Map<Int, SeriesIdMappingEntry>

    private val _entries: List<SeriesIdMappingEntry>

    init {
        ClassPathResource("mapping/anime-list-mini.json")
            .inputStream
            .use { inputStream ->
                _entries = jacksonObjectMapper()
                    .readValue(inputStream, Array<SeriesIdMappingEntry>::class.java)
                    .toList()
            }

        _byKitsuId = _entries.filter { it.kitsuId != null }.associateBy { it.kitsuId!! }
        _byMalId = _entries.filter { it.malId != null }.associateBy { it.malId!! }
        _byAnilistId = _entries.filter { it.anilistId != null }.associateBy { it.anilistId!! }
    }

    fun findById(kitsuId: Int?, malId: Int?, anilistId: Int?): SeriesIdMappingEntry? {
        return when {
            kitsuId != null -> findByKitsuId(kitsuId)
            malId != null -> findByMalId(malId)
            anilistId != null -> findByAnilistId(anilistId)
            else -> null
        }
    }

    fun findByKitsuId(kitsuId: Int): SeriesIdMappingEntry? = _byKitsuId[kitsuId]
    fun findByMalId(malId: Int): SeriesIdMappingEntry? = _byMalId[malId]
    fun findByAnilistId(anilistId: Int): SeriesIdMappingEntry? = _byAnilistId[anilistId]
}
