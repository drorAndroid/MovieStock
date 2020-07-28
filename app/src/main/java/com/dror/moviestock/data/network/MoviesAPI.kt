package com.dror.moviestock.data.network

import com.dror.moviestock.model.MovieWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Single<MovieWrapper>

    @GET("movie/popular")
    fun getMostPopularMovies(@Query("api_key") apiKey: String): Single<MovieWrapper>
}