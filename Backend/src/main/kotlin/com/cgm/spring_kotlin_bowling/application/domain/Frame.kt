package com.cgm.spring_kotlin_bowling.application.domain

data class Frame(
    var id: Int,
    var currentShot: Int,
    var frameShots: ArrayList<Int>,
    var localScore: Int,
    var bonusShots: Int,
    var flag: String,
    var isExpired: Boolean,
    var isUpdated: Boolean
)
