package com.cgm.spring_kotlin_bowling.persistenceModels

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class FramePostgre (@Id var id: Int,
                         var shot1: Int?,
                         var shot2: Int?,
                         var shot3: Int?,
                         var score: Int?,
                         var flag: String = "")