package com.soda1127.example.bookstore.screen

import android.app.Application
import android.content.Context
import com.soda1127.example.bookstore.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BookStoreApp: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BookStoreApp)
            modules(
                viewModelModule,
                networkModule,
                repositoryModule,
                coroutinesModule,
                databaseModule
            )
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {

        var appContext: Context? = null
            private set

    }

}
