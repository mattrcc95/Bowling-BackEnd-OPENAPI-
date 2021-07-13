package com.cgm.spring_kotlin_bowling.application.services

import com.cgm.spring_kotlin_bowling.application.domain.Frame
import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import org.springframework.stereotype.Component

const val bound = 10
const val LAST_FRAME_ID = bound

@Component
class GameApi {
    //logic to play the whole roll, return the scoreboard after the roll
    fun play(game: ArrayList<Frame>): ArrayList<FramePostgre> {
        val currentFrame = game[game.lastIndex]
        assignBonusScore(game, currentFrame.currentShot)
        currentFrame.frameShots.add(currentFrame.currentShot)
        currentFrame.localScore += currentFrame.currentShot
        assessCurrentFrameState(currentFrame)
        return getDataBaseScoreBoard(game)
    }

    //logic to display the update scoreboard
    private fun getDataBaseScoreBoard(game: ArrayList<Frame>): ArrayList<FramePostgre> {
        val list = arrayListOf<FramePostgre>()
        if (game.size > 1) {
            for (i in 0 until game.size - 1) {
                if (game[i].bonusShots == 0 && !game[i + 1].isUpdated) {
                    game[i + 1].localScore += game[i].localScore
                    game[i + 1].isUpdated = true
                }
            }
        }
        game.forEach { frame -> list.add(mapFrameToPostgre(frame)) }
        return list
    }

    //logic to check if a given frame is expired and, eventually, assigning bonusShot != 0
    private fun assessCurrentFrameState(currentFrame: Frame) {
        val localScore = currentFrame.frameShots.fold(0) { sum, shot -> sum + shot }
        if (currentFrame.id < LAST_FRAME_ID) {
            if (currentFrame.frameShots.size == 2 || currentFrame.frameShots[0] == LAST_FRAME_ID) {
                currentFrame.bonusShots = getBonus(currentFrame)
                currentFrame.isExpired = true
            }
        } else {
            if (localScore < LAST_FRAME_ID) {
                if (currentFrame.frameShots.size == 2) {
                    currentFrame.isExpired = true
                }
            } else {
                if (currentFrame.frameShots.size == 3) {
                    currentFrame.isExpired = true
                }
            }
        }
    }

    //logic to assign the correct bonus to each frame completed
    private fun getBonus(frame: Frame): Int {
        val localScore = frame.frameShots.fold(0) { sum, shot -> sum + shot }

        return when {
            (localScore < LAST_FRAME_ID) -> 0
            (frame.frameShots.size == 1) -> 2
            else -> 1
        }
    }

    //logic to assign the associated score for each frame whose bonusShot != 0
    private fun assignBonusScore(game: ArrayList<Frame>, currentShot: Int) {
        for (frame in game) {
            if (frame.bonusShots > 0) {
                frame.localScore += currentShot
                --frame.bonusShots
            }
        }
    }

    fun rollIsValid(rollValue: Int, currentFrame: Frame): Boolean {
        return rollValue in 0..getThreshold(currentFrame)
    }

    fun getThreshold(currentFrame: Frame): Int {
        val localScore = currentFrame.frameShots.sum()
        return when (currentFrame.id) {
            LAST_FRAME_ID -> getThresholdLast(currentFrame, localScore)
            else -> bound - localScore
        }
    }


    private fun getThresholdLast(currentFrame: Frame, localScore: Int): Int {
        return if (currentFrame.frameShots.size < 2 && localScore < 10) {
            bound - localScore
        } else if (currentFrame.frameShots.size == 2 && currentFrame.frameShots[1] < 10)
            2 * bound - localScore
        else {
            bound
        }
    }

    //logic to map a frame to a framePostgre (persistence model) object
    fun mapFrameToPostgre(frame: Frame): FramePostgre {
        val framePostgre = FramePostgre(0, null, null, null, 0, "")
        framePostgre.score = frame.localScore
        framePostgre.id = frame.id
        if (frame.frameShots.size == 1) { //1 shot made
            framePostgre.shot1 = frame.frameShots[0]
            if (frame.frameShots[0] < LAST_FRAME_ID)
                framePostgre.flag = frame.frameShots[0].toString()
            else
                framePostgre.flag = " X "
        } else if (frame.frameShots.size == 2) { // 2 shots made
            framePostgre.shot1 = frame.frameShots[0]
            framePostgre.shot2 = frame.frameShots[1]
            if (frame.frameShots[0] + frame.frameShots[1] < LAST_FRAME_ID) //both shots < 10
                framePostgre.flag = frame.frameShots[0].toString() + " - " + frame.frameShots[1].toString()
            if ((frame.frameShots[0] + frame.frameShots[1] == LAST_FRAME_ID && frame.frameShots[1] != LAST_FRAME_ID)
                || (frame.frameShots[0] == 0 && frame.frameShots[1] == LAST_FRAME_ID)
            ) //spare
                framePostgre.flag = frame.frameShots[0].toString() + " - / "
            if (frame.frameShots[0] == LAST_FRAME_ID && frame.frameShots[1] < LAST_FRAME_ID)
                framePostgre.flag = " X " + " - " + frame.frameShots[1].toString()
            if (frame.frameShots[0] == LAST_FRAME_ID && frame.frameShots[1] == LAST_FRAME_ID)
                framePostgre.flag = " X - X "
        } else { //3 shot made
            framePostgre.shot1 = frame.frameShots[0]
            framePostgre.shot2 = frame.frameShots[1]
            framePostgre.shot3 = frame.frameShots[2]
            if (frame.frameShots[0] == LAST_FRAME_ID) { //first shot: strike
                if (frame.frameShots[1] < LAST_FRAME_ID && frame.frameShots[1] + frame.frameShots[2] == LAST_FRAME_ID) // spare after first strike
                    framePostgre.flag = " X " + " - " + frame.frameShots[1].toString() + " - / "
                else if (frame.frameShots[1] < LAST_FRAME_ID && frame.frameShots[1] + frame.frameShots[2] < LAST_FRAME_ID) //regular after first strike
                    framePostgre.flag =
                        " X " + " - " + frame.frameShots[1].toString() + " - " + frame.frameShots[2].toString()
                else if (frame.frameShots[1] == LAST_FRAME_ID && frame.frameShots[2] == LAST_FRAME_ID) // double strike after first strike
                    framePostgre.flag = " X - X - X "
                else { //strike after first strike, then regular
                    framePostgre.flag = " X - X - " + frame.frameShots[2].toString()
                }
            } else { //first shot: spare
                if (frame.frameShots[2] == LAST_FRAME_ID) //strike after first spare
                    framePostgre.flag = frame.frameShots[0].toString() + " - / " + "-  X "
                else //regular after first spare
                    framePostgre.flag = frame.frameShots[0].toString() + " - / - " + frame.frameShots[2].toString()
            }
        }
        return framePostgre
    }
}