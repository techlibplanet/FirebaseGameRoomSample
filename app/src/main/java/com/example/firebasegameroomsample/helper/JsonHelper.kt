package com.example.firebasegameroomsample.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import java.lang.reflect.Type
import java.util.*


object JsonHelper {
    inline fun <reified T> jsonToKt(jsonString: String): T =
        Gson().fromJson(jsonString, T::class.java)


    fun KtToJson(obj: Any): String = Gson().toJson(obj)


    inline fun <reified T> jsonToKtList(jsonString: String): List<T> =
        Gson().fromJson<List<T>>(jsonString, T::class.java)

    inline fun <reified T> jsonToKtMutableList(jsonString: String): MutableList<T> =
        Gson().fromJson<MutableList<T>>(jsonString, T::class.java)

    fun <T> stringToArray(
        s: String?,
        clazz: Class<Array<T>?>?
    ): MutableList<Array<T>> {
        val arr: Array<T>? = Gson().fromJson(s, clazz)
        return Arrays.asList(arr) //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }

    /*inline fun <reified T> jsonLinkedTreeMapToObject(jsonString: String) : MutableList<T>{
        return Gson().fromJson(Gson().toJson((( jsonString as LinkedTreeMap<String, T>))), T::class.java)
    }*/

}