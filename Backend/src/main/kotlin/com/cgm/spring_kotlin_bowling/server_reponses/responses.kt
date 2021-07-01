package com.cgm.spring_kotlin_bowling.server_reponses

import com.cgm.spring_kotlin_bowling.jsonApiModels.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private typealias JsonApiError = Error

fun negativeFrameFetchingResponse(id: String) =
    buildResponse(HttpStatus.NOT_FOUND, "404", "404 NOT FOUND: frame with ID $id not found")

fun positiveFrameFetchingResponse(frameJson: Frame, id: String): ResponseEntity<Any> {
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

private fun buildResponse(
    status: HttpStatus,
    code: String,
    description: String,
    httpHeaders: HttpHeaders = HttpHeaders()
): ResponseEntity<Any> {
    val errorTemplate = JsonApiError()
    errorTemplate.data = ErrorData()
    errorTemplate.data.attributes = ErrorDataAttributes()
    errorTemplate.data.attributes.code = code
    errorTemplate.data.attributes.description = description
    return ResponseEntity.status(status).headers(httpHeaders).body(errorTemplate)
}

fun positiveRollResponse(rollValue: Int): ResponseEntity<Any> {
    val roll = getRoll(rollValue)
    roll.data.attributes.value = rollValue
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(roll)
}

fun negativeRollResponse(): ResponseEntity<Any> =
    buildResponse400("400 BAD REQUEST: roll not acceptable")

fun gameEndsReponse(): ResponseEntity<Any> =
    buildResponse400("400 BAD REQUEST: game has ended")


fun getRoll(rollValue: Int): Roll {
    val rollAttributes = RollDataAttributes()
    rollAttributes.value = rollValue
    val rollData = RollData()
    rollData.attributes = rollAttributes
    val roll = Roll()
    roll.data = rollData
    return roll
}

fun apiNotImplementedResponse(): ResponseEntity<Any> {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("description", " API not implemented")
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "500 API NOT IMPLEMENTED", httpHeaders)
}
