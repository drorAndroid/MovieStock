package com.dror.moviestock.data.network

import com.dror.moviestock.di.DaggerApplicationComponent
import com.dror.moviestock.model.MovieWrapper
import com.dror.moviestock.utils.API_KEY
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService @Inject constructor(private val moviesAPI: MoviesAPI): BaseAPIService() {

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