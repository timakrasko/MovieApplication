package ua.edu.sumdu.movielibrary

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.edu.sumdu.movielibrary.data.di.appModule

class MovieLibraryApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieLibraryApp)
            androidLogger(Level.DEBUG)

            modules(appModule)
        }
    }
}