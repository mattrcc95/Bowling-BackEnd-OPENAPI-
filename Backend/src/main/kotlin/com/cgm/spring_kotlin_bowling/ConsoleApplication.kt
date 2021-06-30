package com.cgm.spring_kotlin_bowling

import com.cgm.spring_kotlin_bowling.jsonApiModels.Roll
import com.cgm.spring_kotlin_bowling.jsonApiModels.RollData
import com.cgm.spring_kotlin_bowling.jsonApiModels.RollDataAttributes
import com.cgm.spring_kotlin_bowling.persistenceModels.FramePostgre
import com.cgm.spring_kotlin_bowling.server_reponses.gameEndsReponse
import com.cgm.spring_kotlin_bowling.server_reponses.negativeRollResponse
import com.cgm.spring_kotlin_bowling.server_reponses.positiveRollResponse
import com.cgm.spring_kotlin_bowling.service.GameApi
import com.cgm.spring_kotlin_bowling.service.PlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
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
        while(gameKeepsGoing){
            println(message)
            val currRoll = sc.nextInt()
            playerService.playRoll(currRoll)
        }

    }

    //        while (true) {
//            val currShot = 11
//            while (playerService.playRoll(11)) {
//                println(message)
//                roll.data.attributes.value = sc.nextInt()
//                response = playerService.playRoll(roll)
//                if(response == negativeRollResponse()) {
//                    println(errMessage)
//                }
//            }
//            printScoreboard(playerService.getScoreBoard())
//            if(response == gameEndsReponse()) {
//                break
//            }
//        }

    fun printScoreboard (scoreboard : ArrayList<FramePostgre>) {
        scoreboard.forEach { item -> println(printFrame(item)) }
    }

    fun printFrame (frame: FramePostgre) : String = "${frame.id}] :: ${frame.flag} --> ${frame.score}"

}