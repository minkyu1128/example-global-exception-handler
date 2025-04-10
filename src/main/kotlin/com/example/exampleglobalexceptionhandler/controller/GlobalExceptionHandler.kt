package com.example.exampleglobalexceptionhandler.controller

import com.example.exampleglobalexceptionhandler.support.error.DefaultException
import com.example.exampleglobalexceptionhandler.support.response.DefaultErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.example.exampleglobalexceptionhandler"])
class GlobalExceptionHandler {

    @ExceptionHandler(DefaultException::class)
    protected fun handleDefaultException(e: DefaultException): ResponseEntity<DefaultErrorResponse> {
        return DefaultErrorResponse.of(e.error!!)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<DefaultErrorResponse> {
        e.printStackTrace()
        return DefaultErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected error")
    }

}