package com.dror.moviestock.di

import com.dror.moviestock.view.MainActivity
import com.dror.moviestock.model.MoviesRepository
import com.dror.moviestock.data.network.MoviesService
import com.dror.moviestock.viewmodel.MoviesViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(service: MoviesService)
    fun inject(moviesRepository: MoviesRepository)
    fun inject(moviesViewModel: MoviesViewModel)
    fun inject(mainActivity: MainActivity)

}