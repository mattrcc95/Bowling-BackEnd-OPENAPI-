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


//TESTS HEXAGONE SIDE BY SIDE: CONTROLLER -> SERVICE -> REPOS -> DB
@SpringBootTest(classes = [SpringKotlinBowlingApplication::class])
class PlayerControllerTest(
    @Autowired private val playerController: PlayerController
) {

    //           GIVEN    WHEN   THEN
    // AAA:    Arrange    Act    Assert
    @BeforeEach
    fun clearDB() {
        playerController.deleteAllFrames()
    }

    //test the in-bound door
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
    fun `GIVEN a VALID roll and then INVALID roll the latter is rejected`() {
        val roll1 = play(3)
        playerController.play(roll1)
        val roll2 = play(9)

        val responsePOST2 = playerController.play(roll2)

        assertEquals(responsePOST2, negativeRollResponse())
    }

    //GENERATE A SERIES OF ACCEPTABLE ROLLS, THEN VERIFY THAT "GAME ENDS" IS DROPPED AT THE END
    fun `GIVEN sample game, THEN throws "game has ended response" at the end`() {

    }
    //GENERATE SAMPLE GAME, THEN VERIFY THAT THE FINAL SCORE IS CORRECT



}