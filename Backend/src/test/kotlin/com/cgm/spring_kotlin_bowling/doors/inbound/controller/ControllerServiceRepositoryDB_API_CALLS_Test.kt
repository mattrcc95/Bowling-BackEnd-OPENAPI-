package com.cgm.spring_kotlin_bowling.doors.inbound.controller


import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.negativeRollResponse
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.positiveRollResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

const val baseLink = "http://localhost:8080/api/v1"
@SpringBootTest
@AutoConfigureMockMvc //generate configuration for mocking model view controller
class ControllerServiceRepositoryDB_API_CALLS_Test @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @BeforeEach
    fun clearDB() {
        mockMvc.delete("$baseLink/scoreboard/frames")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    fun `GIVEN a VALID roll WHEN it is played THEN positive response is returned`() {
        val roll = setRoll(4)
        val testResponse = mockMvc.post("$baseLink/scoreboard/frames/rolls") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(roll)
        } .andReturn().response.contentAsString
        assertEquals(testResponse, objectMapper.writeValueAsString(positiveRollResponse(4).body))
    }

    @Test
    fun `GIVEN an INVALID roll WHEN it is played THEN negative response is returned`() {
        val roll = setRoll(11)
        val testResponse = mockMvc.post("$baseLink/scoreboard/frames/rolls") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(roll)
        }.andReturn().response.contentAsString
        assertEquals(testResponse, objectMapper.writeValueAsString(negativeRollResponse().body))
    }


    @Test
    fun `GIVEN a valid roll and then invalid WHEN the latter is played THEN it is rejected`() {
        val roll1 = setRoll(3)
        mockMvc.post("$baseLink/scoreboard/frames/rolls") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(roll1)
        }
        val roll2 = setRoll(9)
        val testResponse = mockMvc.post("$baseLink/scoreboard/frames/rolls") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(roll2)
        }.andReturn().response.contentAsString
        assertEquals(testResponse, objectMapper.writeValueAsString(negativeRollResponse().body))
    }

}