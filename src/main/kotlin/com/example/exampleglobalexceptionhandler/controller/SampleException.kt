package com.example.exampleglobalexceptionhandler.controller

import com.example.exampleglobalexceptionhandler.support.error.DefaultException

class SampleException(error: SampleError) : DefaultException(error){
}