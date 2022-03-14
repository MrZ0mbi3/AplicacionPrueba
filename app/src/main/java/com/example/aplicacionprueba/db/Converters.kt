/*package com.example.aplicacionprueba.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.aplicacionprueba.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

@ProvidedTypeConverter
class Converters : Serializable{
    @TypeConverter
    fun toMovieList(value: String?): List<Movie> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Movie?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromMovieList(list: List<Movie?>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<Movie?>?>() {}.type
        return gson.toJson(list,type)
    }
    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(list: List<Int?>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<Int?>?>() {}.type
        return gson.toJson(list,type)
    }
}
*/