package com.cmg.SpringKotlinBowling.service

import com.cmg.SpringKotlinBowling.JsonApiModels.Roll
import com.cmg.SpringKotlinBowling.persistenceModels.FramePostgre
import com.cmg.SpringKotlinBowling.repository.PlayerRepository
import com.cmg.SpringKotlinBowling.springDataModels.Frame

const val bound = 10

class Game_Api(private val playerRepository: PlayerRepository) {
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
        game.forEach { frame -> list.add(this.playerRepository.mapFrameToPostgre(frame)) }
        return list
    }

    //logic to check if a given frame is expired and, eventually, assigning bonusShot != 0
    private fun assessCurrentFrameState(currentFrame: Frame) {
        val localScore = currentFrame.frameShots.fold(0) { sum, shot -> sum + shot }
        if (currentFrame.id < 10) {
            if (currentFrame.frameShots.size == 2 || currentFrame.frameShots[0] == 10) {
                currentFrame.bonusShots = getBonus(currentFrame)
                currentFrame.isExpired = true
            }
        } else {
            if (localScore < 10) {
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
        return if (localScore < 10) {
            0
        } else {
            if (frame.frameShots.size == 1) {
                2
            } else {
                println("im here")
                1
            }
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

    fun rollIsValid(roll: Roll, currentFrame: Frame): Boolean {
        val rollValue = roll.data.attributes.value
        return if (rollValue in 0..getThreshold(currentFrame)) true else false
    }

    private fun getThreshold(currentFrame: Frame): Int {
        val localScore = currentFrame.frameShots.fold(0) { sum, shot -> sum + shot }
        return if (currentFrame.id < 10 || (currentFrame.id == 10 && currentFrame.frameShots.size < 2 && localScore < 10)) {
            bound - localScore
        } else if ((currentFrame.id == 10 && currentFrame.frameShots.size == 2 && currentFrame.frameShots[1] < 10))
            2 * bound - localScore
        else {
            bound
        }
    }
}