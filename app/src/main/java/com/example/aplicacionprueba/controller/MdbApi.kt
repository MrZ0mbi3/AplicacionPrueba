package com.example.aplicacionprueba.controller

import android.util.Log
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.model.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.atomic.AtomicBoolean

class MdbApi(private val data: MdbData) {
    lateinit var sessionId: String

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

    fun getActualMovies(query: String): MutableList<Movie> {
        var movies: MutableList<Movie> = mutableListOf()
        CoroutineScope(Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {
                val call = retrofit.create(MdbService::class.java).getAllMoviesNames(query)
                while (!favoriteMovieLoaded.get()) {
                    delay(100)
                }
                val namesMovies = call.body()
                if (call.isSuccessful) {
                    Log.d("Api MDB", "Se encontraron las peliculas")
                    movies = (namesMovies?.results as MutableList<Movie>)
                    data.addAllMovies(movies)
                    movies.forEach {
                        if (data.actualUser.getFavoriteMovies().contains(it)) {
                            it.isSelected = true
                        }
                    }
                    withContext(Dispatchers.Main.immediate) {
                        callbackActualMovies?.onActualMoviesSuccess(movies)
                    }
                } else {
                    Log.d("Api MDB", "Error no se encontraron las peliculas")
                    withContext(Dispatchers.Main.immediate) {
                        callbackActualMovies?.onActualMoviesError()
                    }
                }
            }
        }
        return movies
    }

    fun requestAuthToken(
        queryToken: String,
        querySession: String,
        userName: String,
        userPassword: String
    ) {
        var token: String? = null
        var sessionIdToken: String? = null
        CoroutineScope(Dispatchers.Main.immediate).launch {
            val call = withContext(Dispatchers.IO) {
                retrofit.create(MdbService::class.java).getTokenAuthentication(queryToken)
            }
            val request = call.body()
            if (call.isSuccessful) {
                token = request?.authToken.toString()
                createSession(querySession, UserAuth(userName, userPassword, token!!))
                Log.d("Api MDB token", "Se consigui token" + token)
            }
        }
    }

    fun createSession(query: String, user: UserAuth) {

        CoroutineScope(Dispatchers.Main.immediate).launch {
            val call = withContext(Dispatchers.IO) {
                retrofit.create(MdbService::class.java).createSession(query, user)
            }
            getSessionId(
                "3/authentication/session/new?api_key=267e487850dbcabfc3958c5f0b40bb10",
                user.request_token
            )
        }

    }

    fun getSessionId(query: String, request_token: String) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            val call = withContext(Dispatchers.IO) {
                retrofit.create(MdbService::class.java)
                    .getSessionId(query, TokenRequested(request_token))
            }
            if (call.isSuccessful) {
                sessionId = call.body()?.session_id.toString()
                getFavoriteMovies(
                    "3/account/{account_id}/favorite/movies?api_key=267e487850dbcabfc3958c5f0b40bb10&session_id=",
                    sessionId
                )

            }
        }
    }

    fun getFavoriteMovies(query: String, sessionId: String): MutableList<Movie> {
        var movies: MutableList<Movie> = mutableListOf()
        CoroutineScope(Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {

                val call =
                    retrofit.create(MdbService::class.java).getFavoriteMovies(query + sessionId)

                if (call.isSuccessful) {

                    movies = call.body()?.results as MutableList<Movie>
                    data.actualUser.addAll(movies)
                }
                favoriteMovieLoaded.set(true)
            }
            callbackFavoriteMovies?.onFavoriteMoviesSuccess(movies)
        }
        return movies
    }

    fun setFavoriteMovie(query: String, movie: Movie, favorite: Boolean) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {
                retrofit.create(MdbService::class.java)
                    .setFavoriteMovie(query, FavoriteMovieMessage("movie", movie.id, favorite))
            }
        }
    }


}