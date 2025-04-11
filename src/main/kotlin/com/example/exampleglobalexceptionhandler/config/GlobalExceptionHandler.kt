package com.example.exampleglobalexceptionhandler.config

import com.example.exampleglobalexceptionhandler.support.error.DefaultError
import com.example.exampleglobalexceptionhandler.support.error.DefaultException
import com.example.exampleglobalexceptionhandler.support.response.DefaultErrorResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.example.exampleglobalexceptionhandler"])
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<DefaultErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { err -> err.defaultMessage.toString() }.toList()
        return DefaultErrorResponse.ofValidate(DefaultError.INVALID_FIELDS, errors)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<DefaultErrorResponse> {
        val errors = ex.constraintViolations.map { err -> err.message }.toList()
        return DefaultErrorResponse.ofValidate(DefaultError.INVALID_PARAMETERS, errors)
    }

    @ExceptionHandler(DefaultException::class)
    protected fun handleDefaultException(e: DefaultException): ResponseEntity<DefaultErrorResponse> {
        return DefaultErrorResponse.of(e.error)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<DefaultErrorResponse> {
        e.printStackTrace()
        return DefaultErrorResponse.of(DefaultError.INTERNAL_SERVER_ERROR)
    }

}