package com.example.exampleglobalexceptionhandler.support.response

import org.springframework.http.ResponseEntity
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

class DefaultResponse<T>(
    override val code: String,
    override val message: String,
    override val timestamp: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
    val data: T
) : IResponse, Serializable {
    companion object {
        private const val serialVersionUID = 1L

        fun ok(): ResponseEntity<DefaultResponse<Unit>> {
            return ResponseEntity.ok(
                DefaultResponse(
                    code = "OK",
                    message = "Success",
                    data = Unit
                )
            )
        }

        fun <T> of(data: T): ResponseEntity<DefaultResponse<T>> {
            return ResponseEntity.ok(
                DefaultResponse(
                    code = "OK",
                    message = "Success",
                    data = data
                )
            )
        }
    }
}
