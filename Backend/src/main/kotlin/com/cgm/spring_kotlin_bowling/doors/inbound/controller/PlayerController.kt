package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.doors.inbound.controller.jsonApiModels.*
import com.cgm.spring_kotlin_bowling.doors.outbound.server.server_reponses.*
import com.cgm.spring_kotlin_bowling.application.services.PlayRollResult
import com.cgm.spring_kotlin_bowling.application.services.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

const val MIME_JSONAPI = "application/vnd.api+json"

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1")
class PlayerController(private val playerService: PlayerService) {
    val baseLink = "http://localhost:8080/api/v1"

    @RequestMapping("/scoreboard/frames", method = [RequestMethod.GET], produces = [MIME_JSONAPI])
    fun getAllFrames(): ResponseEntity<Any> {
        val scoreboard = Scoreboard()
        val links = FrameLinks()
        scoreboard.links = links
        scoreboard.links.self = "$baseLink/scoreboard/frames"
        val frameList = playerService.getScoreBoard()

        val jsonApiFrames = frameList.map { getJsonApiFrame(it) }
        scoreboard.data.addAll(jsonApiFrames)
        return positiveScoreboardFetchingResponse(scoreboard)
    }

    @RequestMapping("/scoreboard/frames", method = [RequestMethod.DELETE], produces = [MIME_JSONAPI])
    fun deleteAllFrames(): ResponseEntity<Any> {
        this.playerService.deleteAll()
        return positiveScoreboardDeletionResponse()
    }

    @RequestMapping("/scoreboard/frames/rolls", method = [RequestMethod.POST], produces = [MIME_JSONAPI])
    fun getRoll(@RequestBody roll: Roll): ResponseEntity<Any> {
        val rollValue = roll.data.attributes.value
        return when(this.playerService.playRoll(rollValue)){
            PlayRollResult.ROLl_ACCEPTED -> positiveRollResponse(rollValue)
            PlayRollResult.ROLL_REJECTED -> negativeRollResponse()
            PlayRollResult.ENDGAME -> gameEndsReponse()
        }
    }

    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.GET], produces = [MIME_JSONAPI])
    fun getFrame(@PathVariable id: String): ResponseEntity<Any> {
        val lastID = this.playerService.getLastId()
        return if ((id == "last" && lastID != 0) || (id.toInt() in 1 until lastID)) {

            val frameJson = if (id == "last") {
                val frame = this.playerService.getLastFrame()
                getJsonApiFrame(frame, "last") //overwrite ID with last
            } else {
                val frame = this.playerService.getFrameByID(id.toInt())
                getJsonApiFrame(frame)
            }
            frameJson.links?.self = "$baseLink/scoreboard/frames/$id"

            positiveFrameFetchingResponse(frameJson, id)
        } else
            negativeFrameFetchingResponse(id)
    }



    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.DELETE], produces = [MIME_JSONAPI])
    fun deleteLastFrame(@PathVariable id: String): ResponseEntity<Any> {
        return apiNotImplementedResponse()
    }

    //NOT IMPLEMENTED APIs:
    @RequestMapping("/scoreboard/frames/", method = [RequestMethod.POST], produces = [MIME_JSONAPI])
    fun createLastFrame(@RequestBody frame: Frame): ResponseEntity<Any> {
        return apiNotImplementedResponse()
    }

    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.PATCH], produces = [MIME_JSONAPI])
    fun updateLastFrame(@PathVariable id: String): ResponseEntity<Any> {
        return apiNotImplementedResponse()
    }

}




