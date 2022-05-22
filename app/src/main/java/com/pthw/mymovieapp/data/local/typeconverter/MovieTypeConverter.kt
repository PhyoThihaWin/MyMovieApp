package com.pthw.mymovieapp.data.local.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pthw.mymovieapp.vos.MovieVO

class MovieTypeConverter {
    @TypeConverter
    fun fromList(list: List<MovieVO>): String? {
        val gson = Gson()
        val type = object : TypeToken<List<MovieVO>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toList(data: String?): List<MovieVO>? {
        val gson = Gson()
        val type = object : TypeToken<List<MovieVO>>() {}.type
        return gson.fromJson<List<MovieVO>>(data ?: "", type)
    }
}