package com.example.exampleglobalexceptionhandler.support.response

import com.example.exampleglobalexceptionhandler.support.error.DefaultError
import com.example.exampleglobalexceptionhandler.support.error.IError
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

@JsonInclude(JsonInclude.Include.NON_NULL)
class DefaultErrorResponse(
    override val code: String,
    override val message: String,
    override val timestamp: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
    val errors: List<String>? = null
) : IResponse, Serializable {
    companion object {
        fun of(error: IError): ResponseEntity<DefaultErrorResponse> {
            return of(error.status(), error.getCode(), error.getMessage(), null)
        }

        fun ofValidate(error: DefaultError, errors: List<String>): ResponseEntity<DefaultErrorResponse> {
            return of(error.status(), error.getCode(), error.getMessage(), errors)
        }

        fun of(
            status: HttpStatus,
            code: String,
            message: String,
            errors: List<String>?
        ): ResponseEntity<DefaultErrorResponse> {
            return ResponseEntity
                .status(status.value())
                .body(
                    DefaultErrorResponse(
                        code = code,
                        message = message,
                        errors = errors
                    )
                )
        }

        fun toResponse(error: IError): DefaultErrorResponse {
            return DefaultErrorResponse(
                code = error.getCode(),
                message = error.getMessage()
            )
        }
    }
}
