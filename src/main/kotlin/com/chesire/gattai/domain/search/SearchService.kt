package com.chesire.gattai.domain.search

import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams

interface SearchService {

    fun search(params: SearchParams): List<SearchModel>
}
