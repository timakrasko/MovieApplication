package ua.edu.sumdu.movielibrary.data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.edu.sumdu.movielibrary.data.Dto.MovieDto
import ua.edu.sumdu.movielibrary.data.repository.FireBaseMovieRepository
import ua.edu.sumdu.movielibrary.data.repository.FireBaseUserRepository
import ua.edu.sumdu.movielibrary.data.repository.MovieRepository
import ua.edu.sumdu.movielibrary.data.repository.OnlineMovieRepository
import ua.edu.sumdu.movielibrary.data.repository.UserRepository
import ua.edu.sumdu.movielibrary.presentation.login.LoginViewModel
import ua.edu.sumdu.movielibrary.presentation.main_screen.MainViewModel
import ua.edu.sumdu.movielibrary.presentation.movie_create.MovieCreateViewModel
import ua.edu.sumdu.movielibrary.presentation.movie_details.MovieDetailsViewModel
import ua.edu.sumdu.movielibrary.presentation.user_profile.UserProfileViewModel

val appModule = module {
    single { Firebase.storage }
    single { Firebase.firestore }
    single { Firebase.auth }
    single<MovieRepository> { FireBaseMovieRepository(get(), get()) }
    single<UserRepository> { FireBaseUserRepository(get(), get()) }
    single<OnlineMovieRepository> {OnlineMovieRepository(get(), get())}
    viewModel {LoginViewModel(get())}
    viewModel { MainViewModel(get()) }
    viewModel { (movie: MovieDto) -> MovieDetailsViewModel(movie, get(), get())}
    viewModel { MovieCreateViewModel(get()) }
    viewModel { (user: String) -> UserProfileViewModel(user, get())}
}