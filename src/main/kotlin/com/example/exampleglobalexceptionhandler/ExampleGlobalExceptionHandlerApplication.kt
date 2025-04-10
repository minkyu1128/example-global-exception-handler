package com.example.exampleglobalexceptionhandler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExampleGlobalExceptionHandlerApplication

fun main(args: Array<String>) {
    runApplication<ExampleGlobalExceptionHandlerApplication>(*args)
}
