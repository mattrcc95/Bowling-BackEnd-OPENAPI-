package com.cmg.SpringKotlinBowling.controller
import com.cmg.SpringKotlinBowling.JsonApiModels.*
import com.cmg.SpringKotlinBowling.persistenceModels.FramePostgre
import com.cmg.SpringKotlinBowling.serverReponses.*
import com.cmg.SpringKotlinBowling.service.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1")
class PlayerController(private val playerService: PlayerService){
    val baseLink = "http://localhost:8080/api/v1"

    @RequestMapping("/scoreboard/frames", method = [RequestMethod.GET], produces = ["application/vnd.api+json"])
    fun getAllFrames() : ResponseEntity<Any> {
        val scoreboard = Scoreboard()
        scoreboard.links.self = "$baseLink/scoreboard/frames"
        val frameList = this.playerService.getScoreBoard()
            frameList.forEach { frame ->
                val dataFrame = DataFrame()
                this.playerService.toJsonApi(frame, dataFrame)
                scoreboard.data.add(dataFrame)
            }
            return positiveScoreboardFetchingResponse(scoreboard)
    }

    @RequestMapping("/scoreboard/frames", method = [RequestMethod.DELETE], produces = ["application/vnd.api+json"] )
    fun deleteAllFrames() : ResponseEntity<Any> {
        this.playerService.deleteAll()
        return positiveScoreboardDeletionResponse()
    }

    @RequestMapping("/scoreboard/frames/rolls", method = [RequestMethod.POST], produces = ["application/vnd.api+json"])
    fun getRoll(@RequestBody roll : Roll) : ResponseEntity<Any> {
        return this.playerService.playRoll(roll)
    }

    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.GET], produces = ["application/vnd.api+json"] )
    fun getFrame(@PathVariable id: String) : ResponseEntity<Any> {
        val lastID = this.playerService.getLastId()
        return if ((id == "last" && lastID != 0) || (id.toInt() in 1 until lastID)) {
            val frameJson = FrameJson()
            frameJson.links?.self = "$baseLink/scoreboard/frames/$id"
            if(id == "last"){
                val frame = this.playerService.getLastFrame()
                this.playerService.toJsonApi(frame, frameJson)
                frameJson.data.id = "last" //overwrite ID with last
            } else {
                val frame = this.playerService.getFrameByID(id.toInt())
                this.playerService.toJsonApi(frame, frameJson)
            }
            positiveFrameFetchingReponse(frameJson, id)
        } else
            negativeFrameFetchingResponse(id)
    }

    //NEW APIs:
    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.DELETE], produces = ["application/vnd.api+json"] )
    fun deleteLastFrame(@PathVariable id: String) : ResponseEntity<Any> {
        val lastID = this.playerService.getLastId()
        return if ((id == "last" && lastID != 0) || (id.toInt()  == lastID)) {
            this.playerService.deleteFrameByID(lastID)
            positiveFrameDeletionResponse()
        } else if (id.toInt() in 0 until lastID){
            negativeFrameDeletionResponseNOTLAST(id)
        } else {
            negativeFrameDeletionResponseNOTFOUND(id)
        }
    }

//    @RequestMapping("/scoreboard/frames/", method = [RequestMethod.POST], produces = ["application/vnd.api+json"] )
//    fun createLastFrame(@RequestBody frame: NoIdFrameJson) : ResponseEntity<Any> {
//        val framePostgre = this.playerService.getFramePostgre_Given_NoIdFrameJson(frame)
//        this.playerService.uploadFrame(framePostgre)
//        val frameJson = FrameJson()
//        this.playerService.toJsonApi(framePostgre, frameJson)
//        return positiveFrameCreationResponse(frameJson)
//    }

//    COMPLETE BELOW..
//    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.PATCH], produces = ["application/vnd.api+json"] )
//    fun updateLastFrame(@PathVariable id: String) : ResponseEntity<Any> {
//
//    }

}




