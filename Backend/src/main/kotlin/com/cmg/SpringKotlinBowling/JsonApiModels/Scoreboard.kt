package com.cmg.SpringKotlinBowling.JsonApiModels

class Scoreboard {
    var links = Links()
    var data = arrayListOf<DataFrame>()
}