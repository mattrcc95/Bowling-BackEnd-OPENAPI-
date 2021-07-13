EX:

WORST CASES:

- "first_strike" - "second_strike" - "complete_regular"
1) delete "complete_regular" and store it
2) "first_strike".localscore -= "complete regular".localscore[0] 
   "second_strike".localscore = "first_strike".localscore + 10
   "second_strike".bonusShot += 2


- "first_strike" - "second_strike" - "half_regular"
1) delete "half_regulare"



//    fun deleteFrameByID(id:Int){
//        playerRepository.deleteById(id)
//        val last = game.removeAt(game.lastIndex)
//        val lastScore = last.frameShots.fold(0) { sum, shot -> sum + shot }
//        val lastLength = last.frameShots.size
//
//        if (game.size > 0) {
//            val secondLast = game[game.lastIndex]
//            secondLast.localScore -= lastScore
//            secondLast.bonusShots += getBonusBack(lastLength, secondLast)
//            if (game.size > 1) {
//                val thirdLast = game[game.lastIndex - 1]
//            }
//        }
//
//    }
//
//    //only for id < 10 up to now
//    fun getBonusBack (lastLength: Int, frame: Frame) : Int {
//        if(frame.flag.contains("X")) {
//            return if (lastLength == 2) 2 else 1
//        } else if(frame.flag.contains("/")){
//            return 1
//        } else {
//            return 0
//        }
//    }


    //NEW APIs:
    //DELETE LAST FRAME
    @RequestMapping("/scoreboard/frames/{id}", method = [RequestMethod.DELETE], produces = [MIME_JSONAPI])
    fun deleteLastFrame(@PathVariable id: String): ResponseEntity<Any> {
//        val lastID = this.playerService.getLastId()
//        return if ((id == "last" && lastID != 0) || (id.toInt()  == lastID)) {
//            this.playerService.deleteFrameByID(lastID)
//            positiveFrameDeletionResponse()
//        } else if (id.toInt() in 0 until lastID){
//            negativeFrameDeletionResponseNOTLAST(id)
//        } else {
//            negativeFrameDeletionResponseNOTFOUND(id)
//        }
return apiNotImplementedResponse()
}

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
//fun positiveFrameCreationResponse(frameJson: FrameJson) : ResponseEntity<Any> {
//    val httpHeaders = HttpHeaders()
//    httpHeaders.add("description", "201 CREATED: roll correctly acquired")
//    return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(frameJson)
//}



