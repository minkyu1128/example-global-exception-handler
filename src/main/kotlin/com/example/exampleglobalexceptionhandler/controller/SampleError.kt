package com.example.exampleglobalexceptionhandler.controller

import com.example.exampleglobalexceptionhandler.support.error.IError
import org.springframework.http.HttpStatus

enum class SampleError(private val httpStatus: HttpStatus, private val message: String) : IError {

    REGISTERED_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 데이터가 없습니다")
    ;

    override fun status(): HttpStatus {
        return this.httpStatus
    }

    override fun getCode(): String {
        return this.name
    }

    override fun getMessage(): String {
        return this.message
    }
}


