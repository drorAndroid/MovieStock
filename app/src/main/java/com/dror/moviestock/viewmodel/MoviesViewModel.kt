package com.dror.moviestock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dror.moviestock.di.DaggerApplicationComponent
import com.dror.moviestock.model.APIResponse
import com.dror.moviestock.model.Movie
import com.dror.moviestock.model.MoviesRepository
import com.dror.moviestock.utils.MovieListUtils
import javax.inject.Inject

class MoviesViewModel: ViewModel() {
    @Inject lateinit var repository: MoviesRepository

    var movies: LiveData<APIResponse<List<Movie>>>
        private set

    var currentMovie = MutableLiveData<Movie?>()
        private set

    init {
        DaggerApplicationComponent.create().inject(this)

        movies = repository.movies
    }

    fun getMovies(list: MovieListUtils.MovieList) {
        if(list == MovieListUtils.MovieList.topRated) {
            repository.getTopRatedMovies()
        }
        else if(list == MovieListUtils.MovieList.mostPopular) {
            repository.getMostPopularMovies()
        }
    }

    fun setCurrentMovie(movie: Movie) {
        currentMovie.value = movie
    }

    override fun onCleared() {
        super.onCleared()

        repository.clear()
    }
}