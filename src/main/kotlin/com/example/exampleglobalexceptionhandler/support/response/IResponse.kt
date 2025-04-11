package com.example.exampleglobalexceptionhandler.support.response

interface IResponse {
    val code: String    //에러 코드
    val message: String //에러 메시지
    val timestamp: Long //타임스탬프
}