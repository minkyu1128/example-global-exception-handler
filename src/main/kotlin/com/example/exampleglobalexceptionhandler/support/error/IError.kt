package com.example.exampleglobalexceptionhandler.support.error

import org.springframework.http.HttpStatus

interface IError {
    fun status(): HttpStatus
    fun getCode(): String
    fun getMessage(): String
}