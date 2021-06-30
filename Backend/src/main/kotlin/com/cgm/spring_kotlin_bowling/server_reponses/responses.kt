package com.cgm.spring_kotlin_bowling.server_reponses

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun negativeFrameFetchingResponse(id: String) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "404"
    errorTemplate.data.attributes.description = "404 NOT FOUND: frame with ID $id not found"
    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(errorTemplate)
}

fun positiveFrameFetchingReponse(frameJson: com.cgm.spring_kotlin_bowling.jsonApiModels.FrameJson, id: String) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "200 OK: frame with ID $id correctly fetched")
    return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(frameJson)
}

fun positiveScoreboardFetchingResponse(scoreboard: com.cgm.spring_kotlin_bowling.jsonApiModels.Scoreboard) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "200 OK: scoreboard correctly fetched")
    return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(scoreboard)
}

fun positiveScoreboardDeletionResponse() : ResponseEntity<Any>{
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "204 NO CONTENT: scoreboard correctly deleted")
    return ResponseEntity.noContent().build()
}

fun positiveRollResponse(roll: com.cgm.spring_kotlin_bowling.jsonApiModels.Roll) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(roll)
}

fun negativeRollResponse(rollValue: Int) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "400"
    errorTemplate.data.attributes.description = "400 BAD REQUEST: roll with value $rollValue is not acceptable"
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(errorTemplate)
}

fun gameEndsReponse() : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "400"
    errorTemplate.data.attributes.description = "400 BAD REQUEST: game has ended"
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(errorTemplate)
}

fun positiveFrameDeletionResponse() : ResponseEntity<Any>{
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "204 NO CONTENT: last frame correctly deleted")
    return ResponseEntity.noContent().build()
}

fun negativeFrameDeletionResponseNOTLAST(id: String) : ResponseEntity<Any>{
    val httpHeaders = HttpHeaders()
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "400"
    errorTemplate.data.attributes.description = "400 BAD REQUEST: frame with ID $id is not the last one"
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(errorTemplate)
}

fun negativeFrameDeletionResponseNOTFOUND(id: String) : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "404"
    errorTemplate.data.attributes.description = "404 NOT FOUND: frame with ID $id not found"
    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(errorTemplate)
}

fun APInotImplementedResponse() : ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", " API not implemented")
    val errorTemplate = com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate()
    errorTemplate.data.attributes.code = "500"
    errorTemplate.data.attributes.description = "500 API NOT IMPLEMENTED"
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(errorTemplate)
}

//fun positiveFrameCreationResponse(frameJson: FrameJson) : ResponseEntity<Any> {
//    val httpHeaders = HttpHeaders()
//    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
//    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(frameJson)
//}