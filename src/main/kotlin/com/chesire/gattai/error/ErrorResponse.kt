package com.chesire.gattai.error

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val details: String? = null,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

data class ValidationErrorResponse(
    val errors: List<ValidationError>,
    val message: String = "Validation failed",
    val details: String = "One or more fields have invalid values",
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    data class ValidationError(
        val field: String,
        val message: String
    )
}
