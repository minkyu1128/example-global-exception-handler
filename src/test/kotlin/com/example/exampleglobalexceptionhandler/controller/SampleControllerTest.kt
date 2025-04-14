package com.example.exampleglobalexceptionhandler.controller

import com.example.exampleglobalexceptionhandler.config.GlobalExceptionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SampleController::class)
@Import(GlobalExceptionHandler::class)
class SampleControllerTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `데이터 목록 조회 - 전체 데이터 조회를 요청하하면 목록 200 OK`() {
        mockMvc.perform(get("/api/datas"))
            .andExpect(status().isOk)
    }

    @Test
    fun `데이터 단건 조회 - 특정 ID로 데이터를 요청하면 상세정보 응답 200 OK`() {
        val id = "123"

        mockMvc.perform(get("/api/datas/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("OK"))
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(jsonPath("$.data['id']").value(id))
            .andExpect(jsonPath("$.data['title']").value("Hello World"))
            .andDo(print())
    }

    @Test
    fun `데이터 등록 - 정상적인 요청일 경우 200 OK`() {
        val request = mapOf(
            "title" to "테스트 제목",
            "description" to "테스트 설명"
        )

        mockMvc.post("/api/datas") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value("OK") }
        }
            .andDo { print() }
    }

    @Test
    fun `데이터 등록 - 제목이 비어있을 경우 400 Bad Request`() {
        val request = mapOf(
            "title" to "",
            "description" to "설명입니다"
        )
        mockMvc.post("/api/datas") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FIELDS") } // 예시
            jsonPath("$.message") { exists() }
        }
            .andDo { print() }
    }

    @Test
    fun `데이터 등록 - 제목이 20자를 초과하면 400 Bad Request`() {
        val request = mapOf(
            "title" to "이것은 매우 매우 매우 긴 제목입니다.", // 20자 초과
            "description" to "정상적인 설명"
        )

        mockMvc.post("/api/datas") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FIELDS") }
        }
            .andDo { print() }
    }

    @Test
    fun `데이터 등록 - 설명이 없을 경우 400 Bad Request`() {
        val request = mapOf(
            "title" to "제목입니다",
            "description" to ""
        )

        mockMvc.post("/api/datas") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.code") { value("INVALID_FIELDS") }
        }
            .andDo { print() }
    }

    @Test
    fun `데이터 수정 - 등록된 데이터가 없어 404 Not Found`() {
        val request = mapOf(
            "title" to "제목 입니다아",
            "description" to "설명입니다아아아아아"
        )

        mockMvc.put("/api/datas/12") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isNotFound() }
                jsonPath("$.code") { value("REGISTERED_DATA_NOT_FOUND") }
            }
            .andDo { print() }
    }

}