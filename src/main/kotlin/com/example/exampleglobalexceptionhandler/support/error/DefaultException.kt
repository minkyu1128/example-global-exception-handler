package com.example.exampleglobalexceptionhandler.support.error

open class DefaultException(error: IError) : RuntimeException(error.getMessage()) {
    var error: IError? = error

}