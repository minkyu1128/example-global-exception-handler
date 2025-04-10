package com.example.exampleglobalexceptionhandler.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SampleController::class)
@Import(GlobalExceptionHandler::class)
class SampleControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `등록된 데이터가 없습니다 예외를 반환한다`() {
        mockMvc.perform(get("/api/datas"))
            .andExpect(status().isNotFound) // SampleError에 맞춰서 NOT_FOUND로 매핑되었다고 가정
            .andExpect(jsonPath("$.code").value(SampleError.REGISTERED_DATA_NOT_FOUND.getCode()))
            .andExpect(jsonPath("$.message").value(SampleError.REGISTERED_DATA_NOT_FOUND.getMessage()))
            .andDo(print())
    }

    @Test
    fun `특정 ID로 데이터를 요청하면 정상 응답을 반환한다`() {
        val id = "123"

        mockMvc.perform(get("/api/datas/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("OK"))
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(jsonPath("$.data['id']").value(id))
            .andExpect(jsonPath("$.data['title']").value("Hello World"))
            .andDo(print())
    }

}