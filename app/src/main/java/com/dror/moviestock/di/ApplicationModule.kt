package com.dror.moviestock.di

import android.app.Application
import com.dror.moviestock.model.MoviesRepository
import com.dror.moviestock.data.network.MoviesAPI
import com.dror.moviestock.data.network.MoviesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule() {
    private val BASE_URL = "https://api.themoviedb.org/3/"


    @Singleton
    @Provides
    fun provideMoviesAPI(): MoviesAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideMoviesService(api: MoviesAPI): MoviesService {
        return MoviesService(api)
    }

    @Singleton
    @Provides
    fun provideMoviesRepository(service: MoviesService): MoviesRepository {
        return MoviesRepository(service)
    }
}