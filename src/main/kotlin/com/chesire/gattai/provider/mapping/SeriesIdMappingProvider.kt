package com.chesire.gattai.provider.mapping

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class SeriesIdMappingProvider {

    private val _byKitsuId: Map<String, SeriesIdMappingEntry>
    private val _byMalId: Map<String, SeriesIdMappingEntry>
    private val _byAnilistId: Map<String, SeriesIdMappingEntry>

    private val _entries: List<SeriesIdMappingEntry>

    init {
        ClassPathResource("mapping/anime-list-mini.json")
            .inputStream
            .use { inputStream ->
                _entries = jacksonObjectMapper()
                    .readValue(inputStream, Array<SeriesIdMappingEntryDto>::class.java)
                    .map {
                        SeriesIdMappingEntry(
                            it.kitsuId?.toString(),
                            it.malId?.toString(),
                            it.anilistId?.toString()
                        )
                    }
                    .toList()
            }

        _byKitsuId = _entries.mapNotNull { entry -> entry.kitsuId?.let { key -> key to entry } }.toMap()
        _byMalId = _entries.mapNotNull { entry -> entry.malId?.let { key -> key to entry } }.toMap()
        _byAnilistId = _entries.mapNotNull { entry -> entry.anilistId?.let { key -> key to entry } }.toMap()
    }

    fun findById(kitsuId: String?, malId: String?, anilistId: String?): SeriesIdMappingEntry? {
        return when {
            kitsuId != null -> findByKitsuId(kitsuId)
            malId != null -> findByMalId(malId)
            anilistId != null -> findByAnilistId(anilistId)
            else -> null
        }
    }

    fun findByKitsuId(kitsuId: String): SeriesIdMappingEntry? = _byKitsuId[kitsuId]
    fun findByMalId(malId: String): SeriesIdMappingEntry? = _byMalId[malId]
    fun findByAnilistId(anilistId: String): SeriesIdMappingEntry? = _byAnilistId[anilistId]
}

private data class SeriesIdMappingEntryDto(
    @JsonProperty("kitsu_id")
    val kitsuId: Int? = null,
    @JsonProperty("mal_id")
    val malId: Int? = null,
    @JsonProperty("anilist_id")
    val anilistId: Int? = null
)
