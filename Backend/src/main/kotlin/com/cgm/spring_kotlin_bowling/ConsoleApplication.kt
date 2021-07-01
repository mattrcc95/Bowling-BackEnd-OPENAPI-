package com.cgm.spring_kotlin_bowling

import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import com.cgm.spring_kotlin_bowling.application.services.PlayRollResult
import com.cgm.spring_kotlin_bowling.application.services.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*

/*
    "quanti birilli hai fatto cadere"
    -> 2
    ---> STAMPA TUTTI I FRAME   |5|2|..|
    "quanti birilli hai fato cadere"
    --> 11
    ---> | | |
 */

const val message: String = "how many pins?"
const val errMessage: String = "unacceptable value!"
const val endsMessage: String = "game has ended!"
val sc = Scanner(System.`in`)

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootConsoleApplication::class.java)
}

@SpringBootApplication
class SpringBootConsoleApplication(
    @Autowired private val playerService: PlayerService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        var gameKeepsGoing = true
        while (gameKeepsGoing) {
            var rollIsValid = false
            while (!rollIsValid) {
                println(message)
                val rollValue = sc.nextInt()

                when(playerService.playRoll(rollValue)) {
                    PlayRollResult.ROLl_ACCEPTED -> rollIsValid = true
                    PlayRollResult.ROLL_REJECTED -> println(errMessage)
                    PlayRollResult.ENDGAME ->  {
                        println(endsMessage)
                        gameKeepsGoing = false
                        break
                    }
                }
            }
            printScoreboard(playerService.getScoreBoard())
        }
    }

    fun printScoreboard(scoreboard: ArrayList<FramePostgre>) {
        scoreboard.forEach { item -> println(printFrame(item)) }
    }

    private fun printFrame(frame: FramePostgre): String = "${frame.id}] :: ${frame.flag} --> ${frame.score}"

}