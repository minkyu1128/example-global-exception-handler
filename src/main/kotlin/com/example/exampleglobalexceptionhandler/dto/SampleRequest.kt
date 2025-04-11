package com.example.exampleglobalexceptionhandler.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SampleRequest(
    @field:NotBlank(message = "제목은 필수조건 입니다")
    @field:Size(max = 20, message = "제목은 20자 이내로 입력하셔야 합니다")
    val title: String?,
    @field:NotBlank(message = "설명은 필수조건 입니다")
    val description: String?,
)