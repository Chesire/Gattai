package com.chesire.gattai.domain

data class Tokens(
    val kitsuToken: String? = null,
    val anilistToken: String? = null,
    val malToken: String? = null
) {

    val hasValidToken: Boolean
        get() = kitsuToken != null || anilistToken != null || malToken != null
}
