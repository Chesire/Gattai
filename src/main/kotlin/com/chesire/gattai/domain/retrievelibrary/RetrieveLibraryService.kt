package com.chesire.gattai.domain.retrievelibrary

import com.chesire.gattai.domain.Tokens

interface RetrieveLibraryService {
    fun extractToken(tokens: Tokens): String?
    fun retrieveLibrary(query: RetrieveLibraryQuery): RetrieveServiceResult
}

sealed interface RetrieveServiceResult {
    data class Success(val models: List<LibraryModel>) : RetrieveServiceResult
    data object NoResults : RetrieveServiceResult
    data class Error(val errorMessage: String) : RetrieveServiceResult
}
