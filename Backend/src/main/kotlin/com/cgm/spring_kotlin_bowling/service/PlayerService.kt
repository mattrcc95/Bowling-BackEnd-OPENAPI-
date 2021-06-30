package com.cgm.spring_kotlin_bowling.service

import com.cgm.spring_kotlin_bowling.persistenceModels.FramePostgre
import com.cgm.spring_kotlin_bowling.repository.PlayerRepository
import com.cgm.spring_kotlin_bowling.server_reponses.gameEndsReponse
import com.cgm.spring_kotlin_bowling.server_reponses.negativeRollResponse
import com.cgm.spring_kotlin_bowling.server_reponses.positiveRollResponse
import com.cgm.spring_kotlin_bowling.spring_data_models.Frame
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.cgm.spring_kotlin_bowling.jsonApiModels.*

@Service
class PlayerService(private val playerRepository: PlayerRepository, private val gameApi: GameApi) {
    private var game = arrayListOf<Frame>()

    fun getScoreBoard(): ArrayList<FramePostgre> = playerRepository.findAll() as ArrayList<FramePostgre>
    fun uploadFrame(framePostgre: FramePostgre): FramePostgre = playerRepository.save(framePostgre)

    fun deleteAll() {
        game = arrayListOf()
        playerRepository.deleteAll()
    }

    fun getLastFrame(): FramePostgre = playerRepository.getLastFrame()
    fun getLastId(): Int = playerRepository.getLastId()
    fun getFrameByID(id: Int): FramePostgre? = playerRepository.findByIdOrNull(id)

//    fun deleteFrameByID(id:Int){
//        playerRepository.deleteById(id)
//        val last = game.removeAt(game.lastIndex)
//        val lastScore = last.frameShots.fold(0) { sum, shot -> sum + shot }
//        val lastLength = last.frameShots.size
//
//        if (game.size > 0) {
//            val secondLast = game[game.lastIndex]
//            secondLast.localScore -= lastScore
//            secondLast.bonusShots += getBonusBack(lastLength, secondLast)
//            if (game.size > 1) {
//                val thirdLast = game[game.lastIndex - 1]
//            }
//        }
//
//    }
//
//    //only for id < 10 up to now
//    fun getBonusBack (lastLength: Int, frame: Frame) : Int {
//        if(frame.flag.contains("X")) {
//            return if (lastLength == 2) 2 else 1
//        } else if(frame.flag.contains("/")){
//            return 1
//        } else {
//            return 0
//        }
//    }

    fun playRoll(roll: Roll): ResponseEntity<Any> {
        val currentRollValue = roll.data.attributes.value
        val currentFrame = Frame(0, currentRollValue, arrayListOf(), 0, 0, "", false, false)
        if (game.isEmpty()) {
            currentFrame.id = 1
            return getRollResponse(roll, currentFrame, true)
        } else {
            val lastFrame = game[game.lastIndex]
            val rollIsPlayable = lastFrame.id < 10 || (lastFrame.id == 10 && !lastFrame.isExpired)
            return if (rollIsPlayable) {
                if (lastFrame.isExpired) {
                    currentFrame.id = lastFrame.id + 1
                    getRollResponse(roll, currentFrame, true)
                } else {
                    lastFrame.currentShot = currentRollValue
                    getRollResponse(roll, lastFrame, false)
                }
            } else gameEndsReponse()
        }
    }

    private fun getRollResponse(
        roll: Roll,
        currentFrame: Frame,
        isToAdd: Boolean
    ): ResponseEntity<Any> {
        return if (gameApi.rollIsValid(roll, currentFrame)) {
            if (isToAdd) game.add(currentFrame)
            val scoreBoard = gameApi.play(game)
            scoreBoard.forEach { framePostgre -> uploadFrame(framePostgre) }
            positiveRollResponse(roll)
        } else negativeRollResponse(roll.data.attributes.value)
    }

    fun <T : Any> toJsonApi(framePostgre: FramePostgre?, frameType: T) {
        if (framePostgre != null) {
            val type = frameType::class.java.simpleName
            if (type == "DataFrame") { //from framePostgre to NoLinkFrame
                val dataFrame = frameType as DataFrame
                dataFrame.id = framePostgre.id.toString()
                setAttributes(framePostgre, dataFrame.attributes)
            } else {
                val frameJson = frameType as FrameJson
                frameJson.data.id = framePostgre.id.toString()
                setAttributes(framePostgre, frameJson.data.attributes)
            }
        }
    }

    private fun setAttributes(
        frame: FramePostgre,
        attributes: AttributesFrame
    ) {
        attributes.shot1 = frame.shot1
        attributes.shot2 = frame.shot2
        attributes.shot3 = frame.shot3
        attributes.localScore = frame.score
        attributes.flag = frame.flag
    }

}

