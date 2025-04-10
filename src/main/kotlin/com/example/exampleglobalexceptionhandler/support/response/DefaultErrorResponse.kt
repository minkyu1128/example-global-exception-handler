package com.example.exampleglobalexceptionhandler.support.response

import com.example.exampleglobalexceptionhandler.support.error.IError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

class DefaultErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
) : IResponse, Serializable {
    companion object {
        fun of(error: IError): ResponseEntity<DefaultErrorResponse> {
            return ResponseEntity
                .status(error.status().value())
                .body(toResponse(error))
        }

        fun of(status: HttpStatus, code: String, message: String): ResponseEntity<DefaultErrorResponse> {
            return ResponseEntity
                .status(status.value())
                .body(
                    DefaultErrorResponse(
                        code = code,
                        message = message
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
