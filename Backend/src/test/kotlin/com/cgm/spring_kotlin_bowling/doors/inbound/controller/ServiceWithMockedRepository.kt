package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.SpringKotlinBowlingApplication
import com.cgm.spring_kotlin_bowling.application.services.PlayRollResult
import com.cgm.spring_kotlin_bowling.application.services.PlayerService
import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import com.cgm.spring_kotlin_bowling.doors.outbound.database.repository.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean


//TESTS HEXAGONE :  SERVICE -> REPOS -> DB
@SpringBootTest(classes = [SpringKotlinBowlingApplication::class])
class ServiceWithMockedRepository(
    @Autowired private val playerService: PlayerService
) {

    @MockBean
    private lateinit var repository: PlayerRepository

    @BeforeEach
    fun mockRepository() {
        `when`(repository.save(any()))
            .thenAnswer {
                FramePostgre(0, null, null, null, 0, "")
            }

        `when`(repository.findAll())
            .thenAnswer {
                listOf(FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, ""))
            }
    }

    //test the in-bound door
    @Test
    fun `GIVEN a VALID roll WHEN it is played THEN positive response is returned`() {
        val rollValue = 4
        val response = playerService.playRoll(rollValue)
        assertEquals(response, PlayRollResult.ROLL_ACCEPTED)
    }

    @Test
    fun `GIVEN an INVALID roll WHEN it is played THEN negative response is returned`() {
        val rollValue = 11
        val response = playerService.playRoll(rollValue)
        assertEquals(response, PlayRollResult.ROLL_REJECTED)
    }

    @Test
    fun `GIVEN a VALID roll and then invalid WHEN the latter is played THEN it is rejected`() {
        val roll1Value = 3
        playerService.playRoll(roll1Value)
        val roll2Value = 8
        val response2 = playerService.playRoll(roll2Value)
        assertEquals(response2, PlayRollResult.ROLL_REJECTED)
    }



}