package com.dror.moviestock.data.network

import com.dror.moviestock.di.DaggerApplicationComponent
import com.dror.moviestock.model.MovieWrapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService @Inject constructor(private val moviesAPI: MoviesAPI): BaseAPIService() {
    override val API_KEY = "0a8ab7ce3f47d45c34ab826104d793fc"

    init {
        DaggerApplicationComponent.create().inject(this)
    }

    fun getTopRatedMovies(): Single<MovieWrapper> {
        return moviesAPI.getTopRatedMovies(API_KEY)
    }

    fun getMostPopularMovies(): Single<MovieWrapper> {
        return moviesAPI.getMostPopularMovies(API_KEY)
    }

    companion object {
        val PHOTO_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}