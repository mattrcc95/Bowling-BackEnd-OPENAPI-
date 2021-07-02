package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.application.domain.Frame
import com.cgm.spring_kotlin_bowling.application.services.GameApi
import com.cgm.spring_kotlin_bowling.application.services.PlayRollResult
import com.cgm.spring_kotlin_bowling.application.services.PlayerService
import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import com.cgm.spring_kotlin_bowling.doors.outbound.database.repository.PlayerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when`

//BEST CASE, SINCE SPRING DOES NOT START AT ALL -> FASTER
class ServiceWitMockedRepositoryNoSpring {
    private val repository = Mockito.mock(PlayerRepository::class.java)
    private val gameApi: GameApi = GameApi()
    private val playerService: PlayerService = PlayerService(repository, gameApi)

    @BeforeEach
    fun mockRepository() {
        `when`(repository.save(any()))
            .thenAnswer {
                FramePostgre(0, null, null, null, 0, "")
            }

        `when`(repository.findAll())
            .thenAnswer {
                arrayListOf(
                    FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, ""),
                    FramePostgre(0, null, null, null, 0, "")
                )
            }

    }

    //test the in-bound door
    @Test
    fun `GIVEN a VALID roll WHEN it is played THEN the roll is accepted`() {
        val rollValue = 4
        val response = playerService.playRoll(rollValue)
        assertEquals(response, PlayRollResult.ROLl_ACCEPTED)
    }

    @Test
    fun `GIVEN an invalid roll WHEN it is played THEN the roll is rejected`() {
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

    //NEW TESTS
    @Test
    fun `GIVEN a getScoreboard() call WHEN it is executed THEN the list of all the frames is returned`() {
        assertEquals(playerService.getScoreBoard(), repository.findAll()) //mocked
    }


    @Test
    fun `GIVEN a Frame WHEN the id is assessed, THEN the correct threshold is computed`() {
        val input = arrayOf(
            Frame(1, 0, arrayListOf(3), 3, 0, "ciao", false, false),
            Frame(10, 0, arrayListOf(10, 10), 3, 0, "ciao", false, false)
        )
        val expected = arrayOf(7, 10)
        for (i in input.indices) {
            assertEquals(gameApi.getThreshold(input[i]), expected[i])
        }
    }


}