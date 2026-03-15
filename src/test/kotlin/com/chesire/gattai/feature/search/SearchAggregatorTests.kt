package com.chesire.gattai.feature.search

import com.chesire.gattai.domain.Ids
import com.chesire.gattai.domain.SeriesType
import com.chesire.gattai.domain.search.SearchService
import com.chesire.gattai.provider.mapping.SeriesIdMappingEntry
import com.chesire.gattai.provider.mapping.SeriesIdMappingProvider
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchAggregatorTests {

    private val searchService = mockk<SearchService>()
    private val searchService2 = mockk<SearchService>()
    private val seriesIdMappingProvider = mockk<SeriesIdMappingProvider>()
    private lateinit var aggregator: SearchAggregator

    @BeforeEach
    fun setup() {
        aggregator = SearchAggregator(listOf(searchService), seriesIdMappingProvider)
    }

    private fun model(
        kitsuId: String? = null,
        malId: String? = null,
        anilistId: String? = null,
        title: String,
        type: SeriesType
    ) = SearchModel(Ids(kitsuId, malId, anilistId), title, type)

    private fun params() = SearchParams("test")

    // -------------------------------------------------------------------------
    // Group 1 — Single service, single result, one ID only
    // -------------------------------------------------------------------------

    @Test
    fun `kitsuIdOnly manga returns single series with kitsuId`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-1", title = "manga1", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-1", result[0].kitsuId)
        assertEquals(null, result[0].malId)
        assertEquals(null, result[0].aniListId)
        assertEquals(listOf("manga1"), result[0].titles)
    }

    @Test
    fun `malIdOnly manga returns single series with malId`() {
        every { searchService.search(any()) } returns listOf(
            model(malId = "mal-1", title = "manga1", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(null, result[0].kitsuId)
        assertEquals("mal-1", result[0].malId)
        assertEquals(null, result[0].aniListId)
    }

    @Test
    fun `anilistIdOnly manga returns single series with anilistId`() {
        every { searchService.search(any()) } returns listOf(
            model(anilistId = "anilist-1", title = "manga1", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(null, result[0].kitsuId)
        assertEquals(null, result[0].malId)
        assertEquals("anilist-1", result[0].aniListId)
    }

    @Test
    fun `kitsuIdOnly anime provider returns null keeps kitsuId only`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-1", title = "anime1", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById("kitsu-1", null, null) } returns null

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-1", result[0].kitsuId)
        assertEquals(null, result[0].malId)
        assertEquals(null, result[0].aniListId)
    }

    @Test
    fun `malIdOnly anime provider returns null keeps malId only`() {
        every { searchService.search(any()) } returns listOf(
            model(malId = "mal-1", title = "anime1", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById(null, "mal-1", null) } returns null

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(null, result[0].kitsuId)
        assertEquals("mal-1", result[0].malId)
        assertEquals(null, result[0].aniListId)
    }

    @Test
    fun `anilistIdOnly anime provider returns null keeps anilistId only`() {
        every { searchService.search(any()) } returns listOf(
            model(anilistId = "anilist-1", title = "anime1", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById(null, null, "anilist-1") } returns null

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(null, result[0].kitsuId)
        assertEquals(null, result[0].malId)
        assertEquals("anilist-1", result[0].aniListId)
    }

    @Test
    fun `kitsuIdOnly anime provider returns full entry fills all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-1", title = "anime1", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById("kitsu-1", null, null) } returns
                SeriesIdMappingEntry(kitsuId = "kitsu-1", malId = "mal-1", anilistId = "anilist-1")

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-1", result[0].kitsuId)
        assertEquals("mal-1", result[0].malId)
        assertEquals("anilist-1", result[0].aniListId)
    }

    // -------------------------------------------------------------------------
    // Group 2 — Single service, single result, two IDs
    // -------------------------------------------------------------------------

    @Test
    fun `kitsuAndMalId manga returns series with both ids`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-2", malId = "mal-2", title = "manga2", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-2", result[0].kitsuId)
        assertEquals("mal-2", result[0].malId)
        assertEquals(null, result[0].aniListId)
    }

    @Test
    fun `kitsuAndAnilistId manga returns series with both ids`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-2", anilistId = "anilist-2", title = "manga2", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-2", result[0].kitsuId)
        assertEquals(null, result[0].malId)
        assertEquals("anilist-2", result[0].aniListId)
    }

    @Test
    fun `malAndAnilistId manga returns series with both ids`() {
        every { searchService.search(any()) } returns listOf(
            model(malId = "mal-2", anilistId = "anilist-2", title = "manga2", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(null, result[0].kitsuId)
        assertEquals("mal-2", result[0].malId)
        assertEquals("anilist-2", result[0].aniListId)
    }

    @Test
    fun `kitsuAndMalId anime provider returns full entry fills anilistId`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-2", malId = "mal-2", title = "anime2", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById("kitsu-2", "mal-2", null) } returns
                SeriesIdMappingEntry(kitsuId = "kitsu-2", malId = "mal-2", anilistId = "anilist-2")

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-2", result[0].kitsuId)
        assertEquals("mal-2", result[0].malId)
        assertEquals("anilist-2", result[0].aniListId)
    }

    @Test
    fun `kitsuAndMalId anime provider returns null anilistId stays null`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-2", malId = "mal-2", title = "anime2", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById("kitsu-2", "mal-2", null) } returns null

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-2", result[0].kitsuId)
        assertEquals("mal-2", result[0].malId)
        assertEquals(null, result[0].aniListId)
    }

    // -------------------------------------------------------------------------
    // Group 3 — Single service, single result, all IDs
    // -------------------------------------------------------------------------

    @Test
    fun `allIds manga provider not called returns all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(
                kitsuId = "kitsu-3",
                malId = "mal-3",
                anilistId = "anilist-3",
                title = "manga3",
                type = SeriesType.MANGA
            )
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-3", result[0].kitsuId)
        assertEquals("mal-3", result[0].malId)
        assertEquals("anilist-3", result[0].aniListId)
        verify { seriesIdMappingProvider wasNot Called }
    }

    @Test
    fun `allIds anime provider not called returns all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(
                kitsuId = "kitsu-3",
                malId = "mal-3",
                anilistId = "anilist-3",
                title = "anime3",
                type = SeriesType.ANIME
            )
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-3", result[0].kitsuId)
        assertEquals("mal-3", result[0].malId)
        assertEquals("anilist-3", result[0].aniListId)
        verify { seriesIdMappingProvider wasNot Called }
    }

    // -------------------------------------------------------------------------
    // Group 4 — Multiple results from one service
    // -------------------------------------------------------------------------

    @Test
    fun `twoResults sharedMalId merged into one entry with all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-4", malId = "mal-4", title = "manga4a", type = SeriesType.MANGA),
            model(malId = "mal-4", anilistId = "anilist-4", title = "manga4a", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-4", result[0].kitsuId)
        assertEquals("mal-4", result[0].malId)
        assertEquals("anilist-4", result[0].aniListId)
        assertEquals(listOf("manga4a"), result[0].titles)
    }

    @Test
    fun `twoResults sharedKitsuId merged into one entry with all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-5", malId = "mal-5", title = "manga5a", type = SeriesType.MANGA),
            model(kitsuId = "kitsu-5", anilistId = "anilist-5", title = "manga5a", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-5", result[0].kitsuId)
        assertEquals("mal-5", result[0].malId)
        assertEquals("anilist-5", result[0].aniListId)
    }

    @Test
    fun `twoResults sharedAnilistId merged into one entry with all ids`() {
        every { searchService.search(any()) } returns listOf(
            model(malId = "mal-6", anilistId = "anilist-6", title = "manga6a", type = SeriesType.MANGA),
            model(kitsuId = "kitsu-6", anilistId = "anilist-6", title = "manga6a", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-6", result[0].kitsuId)
        assertEquals("mal-6", result[0].malId)
        assertEquals("anilist-6", result[0].aniListId)
    }

    @Test
    fun `twoResults noSharedIds returns two separate entries`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-7a", title = "manga7a", type = SeriesType.MANGA),
            model(kitsuId = "kitsu-7b", title = "manga7b", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(2, result.size)
    }

    @Test
    fun `threeResults twoMergeOneStandalone returns two entries`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-8", malId = "mal-8", title = "manga8a", type = SeriesType.MANGA),
            model(malId = "mal-8", anilistId = "anilist-8", title = "manga8a", type = SeriesType.MANGA),
            model(kitsuId = "kitsu-9", title = "manga8b", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(2, result.size)
    }

    @Test
    fun `twoResults sameSeriesDifferentTitles both titles present`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-9", malId = "mal-9", title = "manga9a", type = SeriesType.MANGA),
            model(malId = "mal-9", anilistId = "anilist-9", title = "manga9b", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(2, result[0].titles.size)
        assertTrue(result[0].titles.contains("manga9a"))
        assertTrue(result[0].titles.contains("manga9b"))
    }

    @Test
    fun `twoResults sameSeriesSameTitle title deduplicated`() {
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-10", malId = "mal-10", title = "manga10", type = SeriesType.MANGA),
            model(malId = "mal-10", anilistId = "anilist-10", title = "manga10", type = SeriesType.MANGA)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals(listOf("manga10"), result[0].titles)
    }

    // -------------------------------------------------------------------------
    // Group 5 — Multiple services
    // -------------------------------------------------------------------------

    @Test
    fun `twoServices sharedMalId merged into one entry with all ids`() {
        aggregator = SearchAggregator(listOf(searchService, searchService2), seriesIdMappingProvider)
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-11", malId = "mal-11", title = "anime11", type = SeriesType.ANIME)
        )
        every { searchService2.search(any()) } returns listOf(
            model(malId = "mal-11", anilistId = "anilist-11", title = "anime11", type = SeriesType.ANIME)
        )

        val result = aggregator.findSeries(params())

        assertEquals(1, result.size)
        assertEquals("kitsu-11", result[0].kitsuId)
        assertEquals("mal-11", result[0].malId)
        assertEquals("anilist-11", result[0].aniListId)
        verify { seriesIdMappingProvider wasNot Called }
    }

    @Test
    fun `twoServices noSharedIds returns two separate entries`() {
        aggregator = SearchAggregator(listOf(searchService, searchService2), seriesIdMappingProvider)
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-12a", title = "anime12a", type = SeriesType.ANIME)
        )
        every { searchService2.search(any()) } returns listOf(
            model(anilistId = "anilist-12b", title = "anime12b", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById(any(), any(), any()) } returns null

        val result = aggregator.findSeries(params())

        assertEquals(2, result.size)
    }

    @Test
    fun `twoServices partialOverlap correctly produces three entries`() {
        aggregator = SearchAggregator(listOf(searchService, searchService2), seriesIdMappingProvider)
        every { searchService.search(any()) } returns listOf(
            model(kitsuId = "kitsu-13a", malId = "mal-13a", title = "anime13a", type = SeriesType.ANIME),
            model(kitsuId = "kitsu-13b", title = "anime13b", type = SeriesType.ANIME)
        )
        every { searchService2.search(any()) } returns listOf(
            model(malId = "mal-13a", anilistId = "anilist-13a", title = "anime13a", type = SeriesType.ANIME),
            model(anilistId = "anilist-13c", title = "anime13c", type = SeriesType.ANIME)
        )
        every { seriesIdMappingProvider.findById(any(), any(), any()) } returns null

        val result = aggregator.findSeries(params())

        assertEquals(3, result.size)
    }
}
