package com.cmg.SpringKotlinBowling.repository
import com.cmg.SpringKotlinBowling.persistenceModels.FramePostgre
import com.cmg.SpringKotlinBowling.springDataModels.Frame
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository: CrudRepository<FramePostgre, Int>{
    @Query("SELECT * FROM frame_postgre WHERE id = (SELECT MAX(id) FROM frame_postgre)", nativeQuery = true)
    fun getLastFrame() : FramePostgre

    @Query("SELECT COALESCE(MAX(id),0) FROM frame_postgre", nativeQuery = true)
    fun getLastId() : Int

    //logic to map a frame (springDataModel) to a framePostgre (persistence model) object
    fun mapFrameToPostgre(frame: Frame) : FramePostgre {
        val framePostgre = FramePostgre(0,null,null, null, 0, "")
        framePostgre.score = frame.localScore
        framePostgre.id = frame.id
        if(frame.frameShots.size == 1){ //1 shot made
            framePostgre.shot1 = frame.frameShots[0]
            if(frame.frameShots[0] < 10)
                framePostgre.flag = frame.frameShots[0].toString()
            else
                framePostgre.flag = " X "
        } else if(frame.frameShots.size == 2){ // 2 shots made
            framePostgre.shot1 = frame.frameShots[0]
            framePostgre.shot2 = frame.frameShots[1]
            if (frame.frameShots[0] + frame.frameShots[1] < 10) //both shots < 10
                framePostgre.flag = frame.frameShots[0].toString() + " - " + frame.frameShots[1].toString()
            if ((frame.frameShots[0] + frame.frameShots[1] == 10 && frame.frameShots[1] != 10)
                || (frame.frameShots[0]==0 && frame.frameShots[1]==10) ) //spare
                framePostgre.flag = frame.frameShots[0].toString() + " - / "
            if(frame.frameShots[0] == 10 && frame.frameShots[1] < 10)
                framePostgre.flag = " X " + " - " + frame.frameShots[1].toString()
            if(frame.frameShots[0] == 10 && frame.frameShots[1] == 10)
                framePostgre.flag = " X - X "
        } else { //3 shot made
            framePostgre.shot1 = frame.frameShots[0]
            framePostgre.shot2 = frame.frameShots[1]
            framePostgre.shot3 = frame.frameShots[2]
            if(frame.frameShots[0] == 10){ //first shot: strike
                if(frame.frameShots[1] < 10 && frame.frameShots[1] + frame.frameShots[2] == 10) // spare after first strike
                    framePostgre.flag = " X " + " - " + frame.frameShots[1].toString() + " - / "
                else if(frame.frameShots[1] < 10 && frame.frameShots[1] + frame.frameShots[2] < 10) //regular after first strike
                    framePostgre.flag = " X " + " - " + frame.frameShots[1].toString() + " - " + frame.frameShots[2].toString()
                else if(frame.frameShots[1] == 10 && frame.frameShots[2] == 10) // double strike after first strike
                    framePostgre.flag = " X - X - X "
                else{ //strike after first strike, then regular
                    framePostgre.flag = " X - X - " + frame.frameShots[2].toString()
                }
            } else{ //first shot: spare
                if(frame.frameShots[2] == 10) //strike after first spare
                    framePostgre.flag = frame.frameShots[0].toString() + " - / " + "-  X "
                else //regular after first spare
                    framePostgre.flag = frame.frameShots[0].toString() + " - / - " + frame.frameShots[2].toString()
            }
        }
        return framePostgre
    }
}