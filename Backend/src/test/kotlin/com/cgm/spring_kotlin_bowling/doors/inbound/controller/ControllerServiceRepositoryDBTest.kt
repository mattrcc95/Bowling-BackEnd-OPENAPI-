package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.SpringKotlinBowlingApplication
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.play
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.negativeRollResponse
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.positiveRollResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [SpringKotlinBowlingApplication::class])
class ControllerServiceRepositoryDBTest(
    @Autowired private val playerController: PlayerController
) {

    @BeforeEach
    fun clearDB() {
        playerController.deleteAllFrames()
    }


    @Test
    fun `GIVEN a VALID roll WHEN it is played THEN positive response is returned`() {
        val roll = play(4)
        val responsePOST = playerController.play(roll)
        assertEquals(responsePOST, positiveRollResponse(4))
    }

    @Test
    fun `GIVEN an INVALID roll WHEN it is played THEN negative response is returned`() {
        val roll = play(11)
        val responsePOST = playerController.play(roll)
        assertEquals(responsePOST, negativeRollResponse())
    }

    @Test
    fun `GIVEN a valid roll and then invalid WHEN the latter is played THEN it is rejected`() {
        val roll1 = play(3)
        playerController.play(roll1)
        val roll2 = play(9)

        val responsePOST2 = playerController.play(roll2)

        assertEquals(responsePOST2, negativeRollResponse())
    }

}