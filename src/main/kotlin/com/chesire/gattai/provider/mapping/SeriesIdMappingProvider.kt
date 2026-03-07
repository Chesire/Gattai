package com.chesire.gattai.provider.mapping

class SeriesIdMappingProvider {

    private val _byKitsuId: Map<Int, SeriesIdMappingEntry>
    private val _byMalId: Map<Int, SeriesIdMappingEntry>
    private val _byAnilistId: Map<Int, SeriesIdMappingEntry>

    private val _entries: List<SeriesIdMappingEntry> = listOf(
        SeriesIdMappingEntry(kitsuId = 1, malId = 1, anilistId = 1),
        SeriesIdMappingEntry(kitsuId = 2, malId = 2, anilistId = 2),
        SeriesIdMappingEntry(kitsuId = 3, malId = 3, anilistId = 3)
    )

    init {
        _byKitsuId = _entries.filter { it.kitsuId != null }.associateBy { it.kitsuId!! }
        _byMalId = _entries.filter { it.malId != null }.associateBy { it.malId!! }
        _byAnilistId = _entries.filter { it.anilistId != null }.associateBy { it.anilistId!! }
    }

    fun findByKitsuId(kitsuId: Int): SeriesIdMappingEntry? = _byKitsuId[kitsuId]
    fun findByMalId(malId: Int): SeriesIdMappingEntry? = _byMalId[malId]
    fun findByAnilistId(anilistId: Int): SeriesIdMappingEntry? = _byAnilistId[anilistId]
}
