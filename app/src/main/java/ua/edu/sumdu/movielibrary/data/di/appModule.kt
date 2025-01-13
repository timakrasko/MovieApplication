package ua.edu.sumdu.movielibrary.data.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.Dto.MovieRepository
import ua.edu.sumdu.movielibrary.data.FireBaseRepository
import ua.edu.sumdu.movielibrary.presentation.main_screen.MainViewModel
import ua.edu.sumdu.movielibrary.presentation.movie_create.MovieCreateViewModel
import ua.edu.sumdu.movielibrary.presentation.movie_details.MovieDetailsViewModel

val appModule = module {
    single<MovieRepository> {FireBaseRepository()}

    viewModel { MainViewModel(get()) }
    viewModel { (movie: MovieDto) -> MovieDetailsViewModel(movie) }
    viewModel { MovieCreateViewModel(get()) }
}