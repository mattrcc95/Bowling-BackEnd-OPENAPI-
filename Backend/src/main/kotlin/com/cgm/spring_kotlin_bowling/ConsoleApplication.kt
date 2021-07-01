package com.cgm.spring_kotlin_bowling

import com.cgm.spring_kotlin_bowling.persistenceModels.FramePostgre
import com.cgm.spring_kotlin_bowling.service.PlayerService
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

    //MAP : positiveRollResponse -> 1
    //      negativeRollResponse -> 0
    //      gameEndsResponse -> -1
    override fun run(vararg args: String?) {
        var gameKeepsGoing = true
        while (gameKeepsGoing) {
            var rollIsValid = false
            while (!rollIsValid) {
                println(message)
                val rollValue = sc.nextInt()
                val response = playerService.playRoll(rollValue)
                if (response == 0) {
                    println(errMessage)
                } else if (response == 1) {
                    rollIsValid = true
                } else {
                    println(endsMessage)
                    gameKeepsGoing = false
                    break
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