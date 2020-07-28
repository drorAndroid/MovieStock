package com.dror.moviestock.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dror.moviestock.model.Movie

@Database(entities = [Movie::class], version = 1)
@TypeConverters(MovieConverters::class)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        val MOVIE_DATABASE_NAME = "\"movie-database\""
    }
}