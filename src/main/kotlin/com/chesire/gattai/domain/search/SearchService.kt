package com.chesire.gattai.domain.search

import com.chesire.gattai.feature.search.SearchModel
import com.chesire.gattai.feature.search.SearchParams

interface SearchService {

    fun search(params: SearchParams): SearchServiceResult
}

sealed interface SearchServiceResult {
    data class Success(val searchModels: List<SearchModel>) : SearchServiceResult
    data object NoResults : SearchServiceResult
    data class Error(val errorMessage: String) : SearchServiceResult
}
