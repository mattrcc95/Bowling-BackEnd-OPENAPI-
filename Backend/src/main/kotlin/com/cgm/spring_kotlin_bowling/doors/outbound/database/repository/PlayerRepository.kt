package com.cgm.spring_kotlin_bowling.doors.outbound.database.repository
import com.cgm.spring_kotlin_bowling.doors.outbound.database.persistence_models.FramePostgre
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository: CrudRepository<FramePostgre, Int>{
    @Query("SELECT * FROM frame WHERE id = (SELECT MAX(id) FROM frame)", nativeQuery = true)
    fun getLastFrame() : FramePostgre

    @Query("SELECT COALESCE(MAX(id),0) FROM frame", nativeQuery = true)
    fun getLastId() : Int
}