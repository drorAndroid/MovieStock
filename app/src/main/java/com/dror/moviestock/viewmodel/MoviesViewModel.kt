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

    fun refresh(list: MovieListUtils.MovieList) {
        repository.refresh(list)
    }

    fun getMovies(list: MovieListUtils.MovieList) {
        repository.getMovies(list)
    }

    fun setCurrentMovie(movie: Movie) {
        currentMovie.value = movie
    }

    override fun onCleared() {
        super.onCleared()

        repository.clear()
    }
}