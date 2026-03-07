package com.chesire.gattai.domain.search

import com.chesire.gattai.domain.Series
import com.chesire.gattai.feature.search.SearchParams

interface SearchService {

    fun search(params: SearchParams): List<Series>
}