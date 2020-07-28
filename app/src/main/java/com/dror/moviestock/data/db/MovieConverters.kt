package com.dror.moviestock.data.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.dror.moviestock.utils.MovieListUtils
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken




class MovieConverters {
    private val SEPARATOR = "-"

    @TypeConverter
    fun restoreList(listOfInt: String?): List<Int?>? {
        return Gson().fromJson<List<Int?>>(
            listOfInt,
            object : TypeToken<List<Int?>?>() {}.type
        )
    }

    @TypeConverter
    fun saveList(listOfInt: List<Int?>?): String? {
        return Gson().toJson(listOfInt)
    }

    @TypeConverter
    fun movieListToString(movieList: MutableSet<MovieListUtils.MovieList>): String? =
        movieList.map { it.ordinal }.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun stringToMovieList(movieList: String): MutableSet<MovieListUtils.MovieList> =
        movieList.split(SEPARATOR).map { MovieListUtils.movieListFromInt(it.toInt()) }.toMutableSet()
}