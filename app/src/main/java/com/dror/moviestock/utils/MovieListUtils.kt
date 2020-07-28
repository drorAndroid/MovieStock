package com.dror.moviestock.utils

import android.content.Context
import com.dror.moviestock.R

class MovieListUtils {
    enum class MovieList(val value: Int) {
        topRated(0),
        mostPopular(1)
    }

    companion object {
        fun getMovieList(context: Context): List<String> {
            return listOf(context.getString(R.string.Top_Rated), context.getString(R.string.Most_Popular))
        }

        private val map = MovieList.values().associateBy(MovieList::value)
        fun movieListFromInt(type: Int) = map[type] ?: error("")
    }
}