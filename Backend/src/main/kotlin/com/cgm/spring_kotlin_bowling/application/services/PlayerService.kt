package com.cgm.spring_kotlin_bowling.application.services

import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import com.cgm.spring_kotlin_bowling.doors.outbound.database.repository.PlayerRepository
import com.cgm.spring_kotlin_bowling.application.domain.Frame
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private var playerRepository: PlayerRepository,
    private var gameApi: GameApi
){
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

    fun playRoll(rollValue: Int): PlayRollResult {
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
            } else PlayRollResult.ENDGAME
        }
    }

    private fun getRollResponse(
        rollValue: Int,
        currentFrame: Frame,
        isToAdd: Boolean
    ): PlayRollResult {
        return if (gameApi.rollIsValid(rollValue, currentFrame)) {
            if (isToAdd)
                game.add(currentFrame)
            val scoreBoard = gameApi.play(game)
            scoreBoard.forEach { framePostgre -> uploadFrame(framePostgre) }
            PlayRollResult.ROLl_ACCEPTED
        } else PlayRollResult.ROLL_REJECTED
    }

}

enum class PlayRollResult {
    ROLl_ACCEPTED,
    ROLL_REJECTED,
    ENDGAME
}

