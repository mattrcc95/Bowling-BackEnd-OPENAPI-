package com.cgm.spring_kotlin_bowling.controller

import com.cgm.spring_kotlin_bowling.jsonApiModels.*
import com.cgm.spring_kotlin_bowling.server_reponses.*
import com.cgm.spring_kotlin_bowling.service.PlayerService
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
        scoreboard.links.self = "$baseLink/scoreboard/frames"
        val frameList = playerService.getScoreBoard()
        frameList.forEach { frame ->
            val dataFrame = DataFrame()
            this.playerService.toJsonApi(frame, dataFrame)
            scoreboard.data.add(dataFrame)
        }
        return positiveScoreboardFetchingResponse(scoreboard)
    }

    @RequestMapping("/scoreboard/frames", method = [RequestMethod.DELETE], produces = [MIME_JSONAPI])
    fun deleteAllFrames(): ResponseEntity<Any> {
        this.playerService.deleteAll()
        return positiveScoreboardDeletionResponse()
    }

    @RequestMapping("/scoreboard/frames/rolls", method = [RequestMethod.POST], produces = [MIME_JSONAPI])
    fun getRoll(@RequestBody roll: Roll): ResponseEntity<Any> {
        return this.playerService.playRoll(roll)
    }

    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.GET], produces = [MIME_JSONAPI])
    fun getFrame(@PathVariable id: String): ResponseEntity<Any> {
        val lastID = this.playerService.getLastId()
        return if ((id == "last" && lastID != 0) || (id.toInt() in 1 until lastID)) {
            val frameJson = FrameJson()
            frameJson.links?.self = "$baseLink/scoreboard/frames/$id"
            if (id == "last") {
                val frame = this.playerService.getLastFrame()
                this.playerService.toJsonApi(frame, frameJson)
                frameJson.data.id = "last" //overwrite ID with last
            } else {
                val frame = this.playerService.getFrameByID(id.toInt())
                this.playerService.toJsonApi(frame, frameJson)
            }
            positiveFrameFetchingResponse(frameJson, id)
        } else
            negativeFrameFetchingResponse(id)
    }


    //NEW APIs:
    //DELETE LAST FRAME
    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.DELETE], produces = [MIME_JSONAPI])
    fun deleteLastFrame(@PathVariable id: String): ResponseEntity<Any> {
//        val lastID = this.playerService.getLastId()
//        return if ((id == "last" && lastID != 0) || (id.toInt()  == lastID)) {
//            this.playerService.deleteFrameByID(lastID)
//            positiveFrameDeletionResponse()
//        } else if (id.toInt() in 0 until lastID){
//            negativeFrameDeletionResponseNOTLAST(id)
//        } else {
//            negativeFrameDeletionResponseNOTFOUND(id)
//        }
        return apiNotImplementedResponse()
    }

    //NOT IMPLEMENTED APIs:
    @RequestMapping("/scoreboard/frames/", method = [RequestMethod.POST], produces = [MIME_JSONAPI])
    fun createLastFrame(@RequestBody frame: NoIdFrameJson): ResponseEntity<Any> {
        return apiNotImplementedResponse()
    }

    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.PATCH], produces = [MIME_JSONAPI])
    fun updateLastFrame(@PathVariable id: String): ResponseEntity<Any> {
        return apiNotImplementedResponse()
    }

}




