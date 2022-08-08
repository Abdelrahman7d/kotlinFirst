package com.jsla.kotlinfirst.pogo

open class PersonInfo(
    val name: String,
    val phoneNumber: String,
    var invitationStatus: Int = 0,
    val id : Long = -1) {

    constructor(personInfo: PersonInfo) :
            this(personInfo.name,
                personInfo.phoneNumber,
                personInfo.invitationStatus,
                personInfo.id)
}