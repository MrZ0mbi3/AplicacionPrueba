package com.example.aplicacionprueba.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.repo.MDBRepository
import com.example.aplicacionprueba.repo.MDBRepositoryLocalImp
import com.example.aplicacionprueba.repo.MDBRepositoryRemoteImpl
import kotlinx.coroutines.*

class MovieViewModel(val mdbRepository: MDBRepository) : ViewModel() {

    class Factory(val appContext: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieViewModel(
                MDBRepository(
                    MDBRepositoryLocalImp(appContext),
                    MDBRepositoryRemoteImpl(appContext)
                )
            ) as T
        }
    }

    val errorHandlerActualMovie = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(MovieViewModel::class.java.simpleName, throwable.message ?: "Unexpected error Samy app")
        Log.e(MovieViewModel::class.java.simpleName, throwable.stackTraceToString() ?: "Unexpected error Samy app")
        _movielistModel.value = MovieState.MovieError(throwable)
    }
    val errorHandlerFavoriteMovies = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(MovieViewModel::class.java.simpleName, throwable.message ?: "Unexpected error Samy app")
        Log.e(MovieViewModel::class.java.simpleName, throwable.stackTraceToString() ?: "Unexpected error Samy app")
        _favoriteMovielistModel.value = MovieState.MovieError(throwable)

    }
    val errorHandlerLogIn = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(MovieViewModel::class.java.simpleName, throwable.message ?: "Unexpected error Samy app")
        Log.e(MovieViewModel::class.java.simpleName, throwable.stackTraceToString() ?: "Unexpected error Samy app")
        _logInModel.value = MovieState.MovieError(throwable)
    }

    private val _movielistModel = MutableLiveData<MovieState>()
    val movielistModel: LiveData<MovieState> = _movielistModel

    private val _favoriteMovielistModel = MutableLiveData<MovieState>()
    val favoriteMovielistModel: LiveData<MovieState> = _favoriteMovielistModel

    private val _logInModel = MutableLiveData<MovieState>()
    val logInModel: LiveData<MovieState> = _logInModel

    fun getActualMovies() {

        viewModelScope.launch(errorHandlerActualMovie) {
            _movielistModel.value=MovieState.LoadingMovies(true)
            delay(2000)
            val movies = mdbRepository.getActualMovies("3/discover/movie?api_key=267e487850dbcabfc3958c5f0b40bb10&language=en-US")
            _movielistModel.value=MovieState.LoadingMovies(false)
            _movielistModel.value = MovieState.MovieSuccess(movies)
        }
    }


    fun getFavoriteMovies() {
        var movies:MutableList<Movie>
        viewModelScope.launch(errorHandlerFavoriteMovies) {
            _favoriteMovielistModel.value=MovieState.LoadingMovies(true)
            delay(1000)
                movies = mdbRepository.getFavoriteMovies("3/account/{account_id}/favorite/movies?api_key=267e487850dbcabfc3958c5f0b40bb10&session_id=")
                    .toMutableList()
            _favoriteMovielistModel.value=MovieState.LoadingMovies(false)
            _favoriteMovielistModel.postValue(MovieState.MovieSuccess(movies))
            mdbRepository.adicionarAlUsuarioMovies(movies)
        }
    }
    fun updateFavoriteMovieUser(movie: Movie, isChecked:Boolean){
        viewModelScope.launch(errorHandlerFavoriteMovies) {
            mdbRepository.actualizarPeliculaFavorita(movie,isChecked)
        }
        getFavoriteMovies()


    }
    //log in functions
    fun autenticarUsuario(userName: String, userPassword: String) {
        viewModelScope.launch(errorHandlerLogIn) {
            mdbRepository.autenticarUsuario(userName, userPassword)
        }
    }

    fun verificarUsuario(name: String) {
        viewModelScope.launch(errorHandlerLogIn) {
            mdbRepository.verificarUsuario(name)
        }

    }
}

sealed class MovieState {
    class MovieSuccess(val data: List<Movie>) : MovieState()
    class MovieError(val throwable: Throwable) : MovieState()
    class LoadingMovies(val activate:Boolean) : MovieState()
}