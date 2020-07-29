package com.dror.moviestock.model

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.dror.moviestock.R
import com.dror.moviestock.application.MovieStockApplication
import com.dror.moviestock.data.db.MovieDatabase
import com.dror.moviestock.data.network.MoviesService
import com.dror.moviestock.di.DaggerApplicationComponent
import com.dror.moviestock.utils.MovieListUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
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

    fun refresh(movieList: MovieListUtils.MovieList) {
        if(moviesService.isNetworkAvailable()) {
            disposable.add(
                Observable.just(Unit)
                    .doOnNext {
                        db.movieDao().deleteAll()
                    }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        getMovies(movieList)
                    }
            )
        }
        else {
            updateResponse(Status.ERROR, null, MovieStockApplication.appContext.getString(R.string.No_Internet_Connection))
        }
    }

    fun getMovies(movieList: MovieListUtils.MovieList) {
        val moviesObservable: Single<MovieWrapper> = if(movieList == MovieListUtils.MovieList.topRated) {
            moviesService.getTopRatedMovies()
        }
        else moviesService.getMostPopularMovies()
        val apiObservable =  moviesObservable
            .map {
                it.results
            }
            .toObservable()
            .doOnNext { movies ->
                movies.forEach { it.movieList.add(movieList) }
                db.movieDao().insertAll(movies)
            }
        val dbObservable = db.movieDao().getAll()
            .flatMap { movies ->
                val filteredList = movies.filter { it.movieList.contains(movieList) }

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
                .filter { it.isNotEmpty() }
                .timeout(100, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(remote)
                .firstElement()
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