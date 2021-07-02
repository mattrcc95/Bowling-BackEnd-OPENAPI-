package com.cgm.spring_kotlin_bowling.doors.inbound.controller

import com.cgm.spring_kotlin_bowling.doors.inbound.controller.jsonApiModels.*


//val frame = getFrame(
//    "1", 10, null,
//    null, "X", 10
//)

//fun setRoll(rollValue: Int) : Roll {
//    val attributes = RollDataAttributes()
//    val data = RollData()
//    val roll = Roll()
//    roll.data = data
//    roll.data.attributes = attributes
//    roll.data.attributes.value = rollValue
//    return roll
//}

fun getFrame(
    id: String, shot1: Int?, shot2: Int?,
    shot3: Int?, flag: String, score: Int?
): Frame {
    val frame = Frame()
    frame.data = setData(id, shot1, shot2, shot3, flag, score)
    frame.links = setLink(id)
    return Frame()
}

private fun setData(
    id: String, shot1: Int?, shot2: Int?,
    shot3: Int?, flag: String, score: Int?
): FrameData {
    val data = FrameData()
    data.type = "frame"
    data.id = id
    data.attributes = setAttributes(shot1, shot2, shot3, flag, score)
    return data
}

private fun setLink(id: String): FrameLinks {
    val baseLink = "http://localhost:8080/api/v1/scoreboard/frames/"
    val links = FrameLinks()
    links.self = "$baseLink/$id"
    return links
}

private fun setAttributes(
    shot1: Int?, shot2: Int?,
    shot3: Int?, flag: String, score: Int?
): FrameDataAttributes {
    val attributes = FrameDataAttributes()
    attributes.flag = flag
    attributes.shot1 = shot1
    attributes.shot2 = shot2
    attributes.shot3 = shot3
    attributes.score = score
    return attributes
}