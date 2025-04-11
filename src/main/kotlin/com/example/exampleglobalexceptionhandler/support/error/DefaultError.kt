package com.example.exampleglobalexceptionhandler.support.error

import org.springframework.http.HttpStatus

enum class DefaultError(private val httpStatus: HttpStatus, private val message: String) : IError {

    // -------------------------------------------------------
    // 4xx Errors
    // -------------------------------------------------------
    INVALID_FIELDS(HttpStatus.BAD_REQUEST, "유효하지 않은 필드 값 입니다"),
    INVALID_PARAMETERS(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터 값 입니다"),

    // -------------------------------------------------------
    // 5xx Errors
    // -------------------------------------------------------
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 오류가 발생 했습니다"),
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


