package com.cgm.spring_kotlin_bowling.repository
import com.cgm.spring_kotlin_bowling.persistenceModels.FramePostgre
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository: CrudRepository<FramePostgre, Int>{
    @Query("SELECT * FROM frame_postgre WHERE id = (SELECT MAX(id) FROM frame_postgre)", nativeQuery = true)
    fun getLastFrame() : FramePostgre

    @Query("SELECT COALESCE(MAX(id),0) FROM frame_postgre", nativeQuery = true)
    fun getLastId() : Int
}