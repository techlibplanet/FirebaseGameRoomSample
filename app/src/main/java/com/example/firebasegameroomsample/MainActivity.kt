package com.example.firebasegameroomsample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasegameroomsample.helper.JsonHelper
import com.example.firebasegameroomsample.models.Rooms
import com.example.firebasegameroomsample.models.Users
import com.google.firebase.database.*
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: DatabaseReference

    private val clickables = intArrayOf(R.id.add_user, R.id.create_room, R.id.join_room)

    private val TAG = MainActivity::class.java.simpleName
    private var rooms: Rooms? = null


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
                            val map = child.value as Map<String, Any>
                            val players = map["players"].toString()
                            val type = object : TypeToken<List<Users>>() {}.type
                            val result: List<Users> = JsonHelper.parseArray<List<Users>>(json = players, typeToken = type)
                            println(result)

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

              //  val playerList = JsonHelper.jsonToKtList<List<Any>>(rooms?.players!!)



//                list.add(user2)
//                database.child("rooms").child("players").setValue(JsonHelper.KtToJson(list))
            }
        }
    }
}
