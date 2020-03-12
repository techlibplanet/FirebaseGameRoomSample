package com.example.firebasegameroomsample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasegameroomsample.Constants.ACCEPT_REQUEST
import com.example.firebasegameroomsample.Constants.REJECT_REQUEST
import com.example.firebasegameroomsample.Constants.WAITING_FOR_USER_TO_ACCEPT
import com.example.firebasegameroomsample.helper.JsonHelper
import com.example.firebasegameroomsample.models.Invitations
import com.example.firebasegameroomsample.models.Rooms
import com.example.firebasegameroomsample.models.Users
import com.google.firebase.database.*
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: DatabaseReference

    private val clickables =
        intArrayOf(
            R.id.add_user,
            R.id.create_room,
            R.id.join_room,
            R.id.leave_room,
            R.id.invitation
        )

    private val TAG = MainActivity::class.java.simpleName
    private var rooms: Rooms? = null
    private var invitations: Invitations? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = FirebaseDatabase.getInstance().reference

        for (id in clickables) findViewById<Button>(id).setOnClickListener(this)

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    when (child.key) {
                        "users" -> {
                            val user =
                                dataSnapshot.child("users").child("123").getValue(Users::class.java)
                            user?.email
                        }
                        "rooms" -> {
                            rooms = dataSnapshot.child("rooms").getValue(Rooms::class.java)
//                            val map = child.value as Map<String, Any>
//                            val players = map["players"].toString()
//                            val type = object : TypeToken<List<Users>>() {}.type
//                            val result: List<Users> = JsonHelper.parseArray<List<Users>>(json = players, typeToken = type)
//                            println(result)

                        }
                        "invitations" -> {
                            invitations = dataSnapshot.child("invitations").child("123")
                                .getValue(Invitations::class.java)
                            if (invitations?.inviteTo == "priyank@gmail.com") {
                                AlertDialog.Builder(this@MainActivity).setTitle("Invitations")
                                    .setMessage("${invitations?.inviteFrom} to connect to room...")
                                    .setPositiveButton(
                                        "Accept"
                                    ) { dialog, which ->
                                        database.child("invitations").child("123").child("status")
                                            .setValue(
                                                REJECT_REQUEST
                                            )
                                        dialog.dismiss()
                                    }.setNegativeButton("Reject") { dialog, which ->
                                        database.child("invitations").child("123").child("status")
                                            .setValue(
                                                ACCEPT_REQUEST
                                            )
                                        dialog.dismiss()
                                    }
                                    .show()
                            } else if (invitations?.inviteFrom == "mayank@gmail.com") {
                                if (invitations?.status == REJECT_REQUEST) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "${invitations?.inviteTo} rejected request.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    database.child("invitations").child("123").removeValue()

                                }
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_user -> {
                val user = Users()
                user.email = "mayank@gmail.coom"
                user.userName = "Mayank"
                user.uid = "123"
                database.child("users").child(user.uid).setValue(user)
            }

            R.id.create_room -> {
                val user1 = Users()
                user1.email = "mayank@gmail.coom"
                user1.userName = "Mayank"
                user1.uid = "123"

                val user3 = Users()
                user3.email = "mayank@gmail.coom"
                user3.userName = "Mayank"
                user3.uid = "123"
//
//                room.romId = "room1"
//                room.maxPlayers = "10"
//                room.minPayers = "2"
                val room = Rooms()
                room.joinedPlayers = "1"
                room.maxPlayers = "10"
                room.minPayers = "2"
                room.romId = "123"
                val players = mutableListOf(user1, user3)
                room.players = JsonHelper.KtToJson(players)
                database.child("rooms").setValue(room)
            }

            R.id.join_room -> {
                val user2 = Users()
                user2.email = "priyank@gmail.coom"
                user2.userName = "Priyank"
                user2.uid = "456"

                val type = object : TypeToken<List<Users>>() {}.type
                val result: MutableList<Users> = JsonHelper.parseArray<MutableList<Users>>(
                    json = rooms?.players!!,
                    typeToken = type
                )

                result.add(user2)
                database.child("rooms").child("players").setValue(JsonHelper.KtToJson(result))

            }

            R.id.leave_room -> {
                val user2 = Users()
                user2.email = "priyank@gmail.coom"
                user2.userName = "Priyank"
                user2.uid = "456"

                val type = object : TypeToken<List<Users>>() {}.type
                val result: MutableList<Users> = JsonHelper.parseArray<MutableList<Users>>(
                    json = rooms?.players!!,
                    typeToken = type
                )

                for (user in result) {
                    if (user.email == user2.email) {
                        result.remove(user)
                        break
                    }
                }
                database.child("rooms").child("players").setValue(JsonHelper.KtToJson(result))
            }

            R.id.invitation -> {
                val invitations = Invitations()
                invitations.inviteFrom = "mayank@gmail.com"
                invitations.inviteTo = "priyank@gmail.com"
                invitations.roomId = "123"
                invitations.status = WAITING_FOR_USER_TO_ACCEPT
                database.child("invitations").child("123").setValue(invitations)
            }
        }
    }
}
