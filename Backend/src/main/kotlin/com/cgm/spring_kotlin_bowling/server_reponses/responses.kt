package com.cgm.spring_kotlin_bowling.server_reponses

import com.cgm.spring_kotlin_bowling.jsonApiModels.ErrorTemplate
import com.cgm.spring_kotlin_bowling.jsonApiModels.FrameJson
import com.cgm.spring_kotlin_bowling.jsonApiModels.Roll
import com.cgm.spring_kotlin_bowling.jsonApiModels.Scoreboard
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun negativeFrameFetchingResponse(id: String) =
    buildResponse(HttpStatus.NOT_FOUND,"404", "404 NOT FOUND: frame with ID $id not found")

fun positiveFrameFetchingResponse(frameJson: FrameJson, id: String): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "200 OK: frame with ID $id correctly fetched")
    return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(frameJson)
}

fun positiveScoreboardFetchingResponse(scoreboard: Scoreboard): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "200 OK: scoreboard correctly fetched")
    return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(scoreboard)
}

fun positiveScoreboardDeletionResponse(): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "204 NO CONTENT: scoreboard correctly deleted")
    return ResponseEntity.noContent().build()
}

private fun buildResponse400(description: String) =
    buildResponse(HttpStatus.BAD_REQUEST, "400", description)

private fun buildResponse(status: HttpStatus, code: String, description: String): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    val errorTemplate = ErrorTemplate()
    errorTemplate.data.attributes.code = code
    errorTemplate.data.attributes.description = description
    return ResponseEntity.status(status).headers(httpHeaders).body(errorTemplate)
}

fun positiveRollResponse(roll: Roll): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(roll)
}

fun negativeRollResponse(rollValue: Int): ResponseEntity<Any> =
    buildResponse400("400 BAD REQUEST: roll with value $rollValue is not acceptable")

fun gameEndsReponse(): ResponseEntity<Any> =
    buildResponse400("400 BAD REQUEST: game has ended")

//fun negativeFrameDeletionResponseNOTLAST(id: String)=
//    buildResponse400( "400 BAD REQUEST: frame with ID $id is not the last one")
//
//fun positiveFrameDeletionResponse(): ResponseEntity<Any> {
//    val httpHeaders = HttpHeaders()
//    httpHeaders.add("description", "204 NO CONTENT: last frame correctly deleted")
//    return ResponseEntity.noContent().build()
//}
//
//fun negativeFrameDeletionResponseNOTFOUND(id: String): ResponseEntity<Any> {
//    val httpHeaders = HttpHeaders()
//    val errorTemplate = ErrorTemplate()
//    errorTemplate.data.attributes.code = "404"
//    errorTemplate.data.attributes.description = "404 NOT FOUND: frame with ID $id not found"
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(errorTemplate)
//}

fun apiNotImplementedResponse(): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", " API not implemented")
    val errorTemplate = ErrorTemplate()
    errorTemplate.data.attributes.code = "500"
    errorTemplate.data.attributes.description = "500 API NOT IMPLEMENTED"
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(errorTemplate)
}

//fun positiveFrameCreationResponse(frameJson: FrameJson) : ResponseEntity<Any> {
//    val httpHeaders = HttpHeaders()
//    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
//    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(frameJson)
//}