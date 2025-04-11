package com.example.exampleglobalexceptionhandler.controller

import com.example.exampleglobalexceptionhandler.dto.SampleRequest
import com.example.exampleglobalexceptionhandler.support.response.DefaultResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api")
class SampleController {
    @GetMapping("/datas")
    fun datas(): ResponseEntity<DefaultResponse<List<String>>> {
//        throw DefaultException(error = SampleError.REGISTERED_DATA_NOT_FOUND)
        throw SampleException(SampleError.REGISTERED_DATA_NOT_FOUND)
        return DefaultResponse.of(data = listOf("Hello World"))
    }

    @GetMapping("/datas/{id}")
    fun data(@PathVariable id: String): ResponseEntity<DefaultResponse<Map<String, String>>> {
        return DefaultResponse.of(
            data = mapOf(
                "id" to id,
                "title" to "Hello World",
                "description" to "Welcome to the world",
            )
        )
    }

    @PostMapping("/datas")
    fun data(@Valid @RequestBody request: SampleRequest): ResponseEntity<DefaultResponse<Unit>> {
        return DefaultResponse.ok()
    }
}