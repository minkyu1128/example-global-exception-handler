package com.example.exampleglobalexceptionhandler.support.error


open class DefaultException : RuntimeException {
    var error: IError

    constructor(error: IError) : super(error.getMessage()) {
        this.error = error
    }

    constructor(error: IError, cause: Throwable) : super(error.getMessage(), cause) {
        this.error = error
    }

}