package com.dror.moviestock.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dror.moviestock.di.ApplicationComponent
import com.dror.moviestock.di.ApplicationModule
import com.dror.moviestock.di.DaggerApplicationComponent

class MovieStockApplication: Application() {
    private lateinit var component: ApplicationComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this);

    }
    override fun onCreate() {
        super.onCreate()

        appContext = this
        component = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule())
            .build()
    }


    fun getAppComponent(): ApplicationComponent {
        return component
    }


    companion object {
        lateinit var appContext: Application
    }
}