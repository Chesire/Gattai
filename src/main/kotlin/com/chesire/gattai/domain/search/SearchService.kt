package com.chesire.gattai.domain.search

interface SearchService {

    fun search(query: SearchQuery): SearchServiceResult
}

sealed interface SearchServiceResult {
    data class Success(val searchModels: List<SearchModel>) : SearchServiceResult
    data object NoResults : SearchServiceResult
    data class Error(val errorMessage: String) : SearchServiceResult
}
