package com.example.firebasegameroomsample.helper

import com.google.gson.Gson

object JsonHelper {
    inline fun <reified T> jsonToKt(jsonString: String): T =
        Gson().fromJson(jsonString, T::class.java)


    fun KtToJson(obj: Any): String = Gson().toJson(obj)


    inline fun <reified T> jsonToKtList(jsonString: String): List<T> =
        Gson().fromJson<List<T>>(jsonString, T::class.java)

    inline fun <reified T> jsonToKtMutableList(jsonString: String): MutableList<T> =
        Gson().fromJson<MutableList<T>>(jsonString, T::class.java)

}