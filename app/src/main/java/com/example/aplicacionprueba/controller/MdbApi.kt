package com.example.aplicacionprueba.controller

import android.util.Log
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.db.room.MdbDataBaseRoom
import com.example.aplicacionprueba.db.room.mapper.toUserEntity
import com.example.aplicacionprueba.model.FavoriteMovieMessage
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.model.TokenRequested
import com.example.aplicacionprueba.model.UserAuth
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.atomic.AtomicBoolean

class MdbApi {
     var sessionId: String? = null

    private val favoriteMovieLoaded = AtomicBoolean(false)

    interface CallbackActualMovies {
        fun onActualMoviesSuccess(movieList: List<Movie>)
        fun onActualMoviesError()
    }

    interface CallbackFavoriteMovies {
        fun onFavoriteMoviesSuccess(movieList: List<Movie>)
        fun onFavoriteMoviesError()
    }

    var callbackActualMovies: CallbackActualMovies? = null
    var callbackFavoriteMovies: CallbackFavoriteMovies? = null

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getActualMovies(query: String): List<Movie> {
        var movies: MutableList<Movie> = mutableListOf()
        val call = retrofit.create(MdbService::class.java).getAllMoviesNames(query)
        val namesMovies = call.body()
        return if (call.isSuccessful) {
            Log.d("Api MDB", "Se encontraron las peliculas")
            movies = (namesMovies?.results as MutableList<Movie>)
            movies
        } else {
            Log.e("Api MDB", "Error no se encontraron las peliculas")
            emptyList()
        }
    }

    suspend fun requestAuthToken(
        queryToken: String,
        querySession: String,
        userName: String,
        userPassword: String
    ) {
        var token: String? = null
        var sessionIdToken: String? = null

        val call = retrofit.create(MdbService::class.java).getTokenAuthentication(queryToken)

        val request = call.body()
        if (call.isSuccessful) {
            token = request?.authToken.toString()
            createSession(querySession, UserAuth(userName, userPassword, token!!))
            Log.d("Api MDB token", "Se consigui token" + token)
        }

    }

    suspend fun createSession(query: String, user: UserAuth) {
        val call = retrofit.create(MdbService::class.java).createSession(query, user)
        getSessionId(
            "3/authentication/session/new?api_key=267e487850dbcabfc3958c5f0b40bb10",
            user.request_token)
    }

    suspend fun getSessionId(query: String, request_token: String) {

        val call = retrofit.create(MdbService::class.java)
            .getSessionId(query, TokenRequested(request_token))

        if (call.isSuccessful) {
            sessionId = call.body()?.session_id.toString()
            //getFavoriteMovies("3/account/{account_id}/favorite/movies?api_key=267e487850dbcabfc3958c5f0b40bb10&session_id=", sessionId)

        }

    }

    suspend fun getFavoriteMovies(query: String): MutableList<Movie> {
        var movies: MutableList<Movie> = mutableListOf()


        while (sessionId == null) {
            delay(100)
        }

        val call =
            retrofit.create(MdbService::class.java).getFavoriteMovies(query + sessionId)

        if (call.isSuccessful) {

            movies = call.body()?.results as MutableList<Movie>
            movies.forEach { it.isSelected = true }

        }
        favoriteMovieLoaded.set(true)

        callbackFavoriteMovies?.onFavoriteMoviesSuccess(movies)
        return movies
    }

    suspend fun setFavoriteMovie(query: String, movie: Movie, favorite: Boolean) {
        retrofit.create(MdbService::class.java)
            .setFavoriteMovie(query, FavoriteMovieMessage("movie", movie.id, favorite))

    }


}