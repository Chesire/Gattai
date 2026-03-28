package com.chesire.gattai.domain

data class Tokens(
    val kitsuToken: String? = null,
    val anilistToken: String? = null,
    val malToken: String? = null
) {

    val hasValidToken: Boolean
        get() = kitsuToken != null || anilistToken != null || malToken != null

    override fun toString(): String {
        return "Tokens(" +
            "kitsuToken=${if (kitsuToken != null) "[REDACTED]" else "null"}, " +
            "anilistToken=${if (anilistToken != null) "[REDACTED]" else "null"}, " +
            "malToken=${if (malToken != null) "[REDACTED]" else "null"})"
    }
}
