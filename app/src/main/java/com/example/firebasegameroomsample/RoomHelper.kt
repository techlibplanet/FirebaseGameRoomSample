package com.example.firebasegameroomsample

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebasegameroomsample.Constants.ACCEPT
import com.example.firebasegameroomsample.Constants.REJECT
import com.example.firebasegameroomsample.helper.JsonHelper
import com.example.firebasegameroomsample.models.Invitations
import com.example.firebasegameroomsample.models.Rooms
import com.example.firebasegameroomsample.models.Users
import com.google.firebase.database.*

class RoomHelper : ValueEventListener {
    private val TAG = RoomHelper::class.java.simpleName
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var rooms: Rooms? = null
    private var invitations: Invitations? = null
    private lateinit var context: Context

    fun addEventValueListener(context: Context) {
        this.context = context
        database.addValueEventListener(this)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.w(TAG, "Failed to read value.", error.toException());
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        for (child in dataSnapshot.children) {
            when (child.key) {
                USERS -> {
                    val user =
                        dataSnapshot.child("users").child("123").getValue(Users::class.java)
                    user?.email
                }
                ROOMS -> {
                    rooms = dataSnapshot.child("rooms").getValue(Rooms::class.java)
                }
                INVITATIONS -> {
                    invitations = dataSnapshot.child("invitations").child("123")
                        .getValue(Invitations::class.java)
                    if (invitations?.inviteTo == "priyank@gmail.com" && invitations?.status == Constants.PENDING) {
                        AlertDialog.Builder(context).setTitle("Invitations")
                            .setMessage("${invitations?.inviteFrom} to connect to room...")
                            .setPositiveButton(
                                "Accept"
                            ) { dialog, which ->
                                acceptInvite("123")
                                dialog.dismiss()
                            }.setNegativeButton("Reject") { dialog, which ->
                                rejectInvite("123")
                                dialog.dismiss()
                            }
                            .show()
                    } else if (invitations?.inviteFrom == "mayank@gmail.com") {
                        if (invitations?.status == REJECT) {
                            Toast.makeText(
                                context,
                                "${invitations?.inviteTo} rejected request.",
                                Toast.LENGTH_LONG
                            ).show()
                            removeInvitation("123")

                        }
                        if (invitations?.status == ACCEPT) {
                            Toast.makeText(
                                context,
                                "${invitations?.inviteTo} accepted request.",
                                Toast.LENGTH_LONG
                            ).show()
                            removeInvitation("123")
                        }
                    }
                }
            }
        }
    }

    fun createRoom(room: Any) = database.child(ROOMS).setValue(room)


    fun updateRoom(list: MutableList<Users>) =
        database.child(ROOMS).child(PLAYERS).setValue(JsonHelper.KtToJson(list))


    fun sendInvite(userId: String, invitations: Any) =
        database.child(INVITATIONS).child(userId).setValue(invitations)


    fun acceptInvite(userId: String) =
        database.child(INVITATIONS).child(userId).child(STATUS).setValue(ACCEPT)


    fun rejectInvite(userId: String) =
        database.child(INVITATIONS).child(userId).child(STATUS).setValue(REJECT)

    fun removeInvitation(userId: String) = database.child(INVITATIONS).child(userId).removeValue()

    companion object {
        const val INVITATIONS = "invitations"
        const val USERS = "users"
        const val ROOMS = "rooms"
        const val PLAYERS = "players"
        const val STATUS = "status"
    }
}
