package com.cgm.spring_kotlin_bowling.service

import com.cgm.spring_kotlin_bowling.persistenceModels.FramePostgre
import com.cgm.spring_kotlin_bowling.repository.PlayerRepository
import com.cgm.spring_kotlin_bowling.spring_data_models.Frame
import org.springframework.data.repository.findByIdOrNull
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


    //MAP : positiveRollResponse -> 1
    //      negativeRollResponse -> 0
    //      gameEndsResponse -> -1
    fun playRoll(rollValue: Int): Int {
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
            } else -1
        }
    }

    private fun getRollResponse(
        rollValue: Int,
        currentFrame: Frame,
        isToAdd: Boolean
    ): Int {
        return if (gameApi.rollIsValid(rollValue, currentFrame)) {
            if (isToAdd)
                game.add(currentFrame)
            val scoreBoard = gameApi.play(game)
            scoreBoard.forEach { framePostgre -> uploadFrame(framePostgre) }
            1
        } else 0
    }

}

