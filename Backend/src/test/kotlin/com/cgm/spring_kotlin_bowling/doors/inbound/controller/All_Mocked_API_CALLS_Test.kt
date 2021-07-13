package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.application.services.PlayerService
import com.cgm.spring_kotlin_bowling.doors.inbound.controller.jsonApiModels.FrameLinks
import com.cgm.spring_kotlin_bowling.doors.inbound.controller.jsonApiModels.Scoreboard
import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import com.cgm.spring_kotlin_bowling.doors.outbound.database.repository.PlayerRepository
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.getJsonApiFrame
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc //generate configuration for mocking model view controller
class All_Mocked_API_CALLS_Test @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val service: PlayerService
) {
    //mockbean -> tells spring to inject repo into service (only SpringBoot context)
    //mock -> shorthand for Mockito.mock()
    @MockBean
    private lateinit var repository: PlayerRepository

    val baseLink = "http://localhost:8080/api/v1"

    @BeforeEach
    fun mock() {
        Mockito.`when`(repository.findAll())
            .thenAnswer {
                List<FramePostgre>(10) {
                        i: Int -> FramePostgre(i, 10, null, null, null, "")
                }
            }
    }

    @Test
    fun `GIVEN a getScoreboard() call, WHEN it is executed, THEN all frames are returned`() {
        val testResponse = mockMvc.get("$baseLink/scoreboard/frames")
            .andExpect {
                status { isOk() }
                content { contentType("application/vnd.api+json") }
            }
            .andReturn().response.contentAsString
        assertEquals(testResponse, getExpectedScoreboard())
    }

    fun getExpectedScoreboard(): String {
        val scoreboard = Scoreboard()
        val frameList = repository.findAll()
        val jsonApiFrames = frameList.map { item -> getJsonApiFrame(item) }
        scoreboard.apply {
            this.links = FrameLinks()
            this.links.self = "$baseLink/scoreboard/frames"
            this.data.addAll(jsonApiFrames)
        }
        return objectMapper.writeValueAsString(scoreboard)
    }

}