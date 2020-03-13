package com.example.firebasegameroomsample

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasegameroomsample.Constants.PENDING
import com.example.firebasegameroomsample.helper.JsonHelper
import com.example.firebasegameroomsample.models.Invitations
import com.example.firebasegameroomsample.models.Rooms
import com.example.firebasegameroomsample.models.Users
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var roomHelper: RoomHelper

    private val clickables =
        intArrayOf(
            R.id.add_user,
            R.id.create_room,
            R.id.join_room,
            R.id.leave_room,
            R.id.invitation
        )

    private var rooms: Rooms? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomHelper = RoomHelper()

        for (id in clickables) findViewById<Button>(id).setOnClickListener(this)

        roomHelper.addEventValueListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_user -> {
                val user = Users()
                user.email = "mayank@gmail.coom"
                user.userName = "Mayank"
                user.uid = "123"
                //database.child("users").child(user.uid).setValue(user)
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
                roomHelper.createRoom(room)
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
                roomHelper.updateRoom(result)

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
                roomHelper.updateRoom(result)
            }

            R.id.invitation -> {
                roomHelper.sendInvite(
                    "123",
                    Invitations("mayank@gmail.com", "priyank@gmail.com", "123", PENDING)
                )
            }
        }
    }
}
