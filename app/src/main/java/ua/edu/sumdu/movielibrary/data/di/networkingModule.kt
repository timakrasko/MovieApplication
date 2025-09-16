package ua.edu.sumdu.movielibrary.data.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.edu.sumdu.movielibrary.data.remote.MovieService

val networkingModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(MovieService::class.java) }
}