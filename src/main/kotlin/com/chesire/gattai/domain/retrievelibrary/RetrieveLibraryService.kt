package com.chesire.gattai.domain.retrievelibrary

import com.chesire.gattai.domain.SeriesType

interface RetrieveLibraryService {

    fun retrieveLibrary(seriesType: SeriesType): RetrieveServiceResult
}

sealed interface RetrieveServiceResult {
    data class Success(val models: List<LibraryModel>) : RetrieveServiceResult
    data object NoResults : RetrieveServiceResult
    data class Error(val errorMessage: String) : RetrieveServiceResult
}
