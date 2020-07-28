package com.dror.moviestock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dror.moviestock.utils.MovieListUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    @Expose(deserialize = false, serialize = false)
    @PrimaryKey(autoGenerate = true)
    val movieId: Long = 0,
    @SerializedName("id")
    val recordId: Int = 0,
    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Double = 0.0,
    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    val voteCount: Int = 0,
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String = "",
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String = "",
    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    val originalLanguage: String = "",
    @SerializedName("genre_ids")
    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int> = listOf(),
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String = "",
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String = "",
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: String = "",
    @Expose(deserialize = false, serialize = false)
    val movieList: MutableSet<MovieListUtils.MovieList> = mutableSetOf()
)