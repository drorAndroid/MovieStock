package com.dror.moviestock.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.dror.moviestock.application.MovieStockApplication
import com.dror.moviestock.data.db.MovieDatabase
import com.dror.moviestock.di.DaggerApplicationComponent
import com.dror.moviestock.data.network.MoviesService
import com.dror.moviestock.utils.MovieListUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val moviesService: MoviesService) {
    val movies = MutableLiveData<APIResponse<List<Movie>>>()
    private val disposable = CompositeDisposable()
    private var db: MovieDatabase

    init {
        DaggerApplicationComponent.create().inject(this)

        db = Room.databaseBuilder(
                MovieStockApplication.appContext,
                MovieDatabase::class.java, MovieDatabase.MOVIE_DATABASE_NAME)
                .build()
    }

    fun getTopRatedMovies() {
        val apiObservable = moviesService.getTopRatedMovies()
            .map {
                it.results
            }
            .toObservable()
            .doOnNext { movies ->
                movies.forEach { it.movieList.add(MovieListUtils.MovieList.topRated) }
                db.movieDao().insertAll(movies)
            }
        val dbObservable = db.movieDao().getAll()
            .flatMap { movies ->
                val filteredList = movies.filter { it.movieList.contains(MovieListUtils.MovieList.topRated) }
                if(filteredList.isEmpty()) return@flatMap apiObservable

                return@flatMap Single.just(filteredList).toObservable()
            }

        observeData(dbObservable, apiObservable)
    }

    fun getMostPopularMovies() {
        val apiObservable = moviesService.getMostPopularMovies()
            .map {
                it.results
            }.toObservable()
            .doOnNext { movies ->
                movies.forEach { it.movieList.add(MovieListUtils.MovieList.mostPopular) }
                db.movieDao().insertAll(movies)
            }
        val dbObservable = db.movieDao().getAll()
            .flatMap { movies ->
                val filteredList = movies.filter { it.movieList.contains(MovieListUtils.MovieList.mostPopular) }
                if(filteredList.isEmpty()) return@flatMap apiObservable

                return@flatMap Single.just(filteredList).toObservable()
            }

        observeData(dbObservable, apiObservable)
    }

    private fun updateResponse(status: Status, data: List<Movie>? = null, errorMessage: String? = null) {
        val responseLoading = APIResponse(status, data, errorMessage)
        movies.value = responseLoading
    }

    private fun observeData(db: Observable<List<Movie>>, remote: Observable<List<Movie>>) {
        disposable.add(
            Observable.concat(db, remote)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateResponse(Status.LOADING)
                }
                .subscribe(
                    {
                        updateResponse(Status.SUCCESS, it)
                    },
                    {
                        updateResponse(Status.ERROR, null, it.localizedMessage)
                    }

                ))
    }

    fun clear() {
        disposable.clear()
    }

}