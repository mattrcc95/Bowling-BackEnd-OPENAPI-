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

    fun playRoll(rollValue: Int): ResponseEntity<Any> {
        val currentFrame = Frame(0, rollValue, arrayListOf(), 0, 0, "", false, false)
        if (game.isEmpty()) {
            currentFrame.id = 1
            return getRollResponse(rollValue, currentFrame, true)
        } else {
            val lastFrame = game[game.lastIndex]
            val rollIsPlayable = lastFrame.id < 10 || (lastFrame.id == 10 && !lastFrame.isExpired)
            return if (rollIsPlayable) {
                if (lastFrame.isExpired) {
                    currentFrame.id = lastFrame.id + 1
                    getRollResponse(rollValue, currentFrame, true)
                } else {
                    lastFrame.currentShot = rollValue
                    getRollResponse(rollValue, lastFrame, false)
                }
            } else gameEndsReponse()
        }
    }

    private fun getRollResponse(
        rollValue: Int,
        currentFrame: Frame,
        isToAdd: Boolean
    ): ResponseEntity<Any> {
        return if (gameApi.rollIsValid(rollValue, currentFrame)) {
            if (isToAdd) game.add(currentFrame)
            val scoreBoard = gameApi.play(game)
            scoreBoard.forEach { framePostgre -> uploadFrame(framePostgre) }
            positiveRollResponse(rollValue)
        } else negativeRollResponse()
    }

    fun <T : Any> toJsonApi(framePostgre: FramePostgre?, frameType: T) {
        if (framePostgre != null) {
            val type = frameType::class.java.simpleName
                val dataFrame = frameType as FrameData
                dataFrame.id = framePostgre.id.toString()
                setAttributes(framePostgre, dataFrame.attributes)
        }
    }

    private fun setAttributes(
        frame: FramePostgre,
        attributes: FrameDataAttributes
    ) {
        attributes.shot1 = frame.shot1
        attributes.shot2 = frame.shot2
        attributes.shot3 = frame.shot3
        attributes.score = frame.score
        attributes.flag = frame.flag
    }

}

