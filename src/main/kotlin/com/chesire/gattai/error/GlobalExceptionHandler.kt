package com.chesire.gattai.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        logger.error("Validation error", ex)

        val validationErrors = ex.bindingResult.fieldErrors.map { fieldError ->
            ValidationErrorResponse.ValidationError(
                field = fieldError.field,
                message = fieldError.defaultMessage ?: "Invalid value"
            )
        }

        val errorResponse = ValidationErrorResponse(errors = validationErrors)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleError(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error", ex)

        val errorResponse = ErrorResponse(
            message = "An unexpected error occurred",
            details = "Please try again later or contact support if the problem persists"
        )

        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
