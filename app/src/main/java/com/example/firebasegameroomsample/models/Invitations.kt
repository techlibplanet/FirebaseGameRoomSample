package com.example.firebasegameroomsample.models

class Invitations(
    val inviteFrom: String,
    val inviteTo: String,
    val roomId: String,
    val status: String
) {
    constructor() : this("", "", "", "")
}