package com.cmg.SpringKotlinBowling.service

import com.cmg.SpringKotlinBowling.JsonApiModels.*
import com.cmg.SpringKotlinBowling.persistenceModels.FramePostgre
import com.cmg.SpringKotlinBowling.springDataModels.Frame
import com.cmg.SpringKotlinBowling.repository.PlayerRepository
import com.cmg.SpringKotlinBowling.serverReponses.gameEndsReponse
import com.cmg.SpringKotlinBowling.serverReponses.negativeRollResponse
import com.cmg.SpringKotlinBowling.serverReponses.positiveRollResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList

@Service
class PlayerService(private val playerRepository: PlayerRepository, private val gameApi: Game_Api) {
    var game = arrayListOf<Frame>()

    fun getScoreBoard(): ArrayList<FramePostgre> = this.playerRepository.findAll() as ArrayList<FramePostgre>
    fun uploadFrame(framePostgre: FramePostgre): FramePostgre = this.playerRepository.save(framePostgre)

    fun deleteAll() {
        game = arrayListOf()
        this.playerRepository.deleteAll()
    }
    fun getLastFrame(): FramePostgre = this.playerRepository.getLastFrame()
    fun getLastId(): Int = this.playerRepository.getLastId()
    fun getFrameByID(id: Int): FramePostgre? = this.playerRepository.findByIdOrNull(id)
    fun deleteFrameByID(id:Int) {
        this.playerRepository.deleteById(id)
        game.removeAt(game.lastIndex)
    }

    fun playRoll(roll: Roll) : ResponseEntity<Any> {
        val currentRollValue = roll.data.attributes.value
        val currentFrame = Frame(0, currentRollValue, arrayListOf(), 0, 0, false, false)
        if(game.isEmpty()){
            currentFrame.id = 1
            return getRollResponse(roll, currentFrame, true)
        } else{
            val lastFrame = game[game.lastIndex]
            val rollIsPlayable = lastFrame.id < 10 || (lastFrame.id == 10 && !lastFrame.isExpired)
            return if(rollIsPlayable) {
                if (lastFrame.isExpired) {
                    currentFrame.id = lastFrame.id + 1
                    getRollResponse(roll, currentFrame, true)
                }
                else {
                    lastFrame.currentShot = currentRollValue
                    getRollResponse(roll, lastFrame, false)
                }
            } else gameEndsReponse()
        }
    }

    private fun getRollResponse(roll: Roll, currentFrame: Frame, isToAdd: Boolean) : ResponseEntity<Any>{
        return if(this.gameApi.rollIsValid(roll, currentFrame)) {
            if(isToAdd) game.add(currentFrame)
            val scoreBoard = this.gameApi.play(game)
            scoreBoard.forEach { framePostgre -> uploadFrame(framePostgre) }
            positiveRollResponse(roll)
        } else negativeRollResponse(roll.data.attributes.value)
    }

     fun <T: Any> toJsonApi(framePostgre: FramePostgre?, frameType: T) {
        if(framePostgre != null) {
            val type = frameType::class.java.simpleName
            if (type == "DataFrame") { //from framePostgre to NoLinkFrame
                val dataFrame = frameType as DataFrame
                dataFrame.id = framePostgre.id.toString()
                setAttributes(framePostgre, dataFrame.attributes)
            } else
            {
                val frameJson = frameType as FrameJson
                frameJson.data.id = framePostgre.id.toString()
                setAttributes(framePostgre, frameJson.data.attributes)
            }
        }
    }

    private fun setAttributes (frame: FramePostgre, attributes: AttributesFrame){
        attributes.shot1 = frame.shot1
        attributes.shot2 = frame.shot2
        attributes.shot3 = frame.shot3
        attributes.localScore = frame.score
        attributes.flag = frame.flag
    }


}

