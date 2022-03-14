package com.example.aplicacionprueba.db

import com.example.aplicacionprueba.db.room.MdbDataBaseRoom
import com.example.aplicacionprueba.db.room.mapper.toUserEntity
import com.example.aplicacionprueba.model.DbUsers
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MdbData(val roomDB: MdbDataBaseRoom) {
    val observers = mutableListOf<MdbDataObservers>()
    val movies = mutableListOf<Movie>()
    lateinit var actualUser: User
    val dbUsers: DbUsers = DbUsers(mutableListOf())
    //lateinit var sessionId: String

    interface MdbDataObservers {
        fun onFavoriteUpdate(listMovies: List<Movie>)
    }
    fun isActualUserInitialized() = ::actualUser.isInitialized

    fun updateFavoriteMovie(movie: Movie, save: Boolean) {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {
                if (save) {
                    actualUser.addFavoriteMovie(movie)

                } else {
                    actualUser.removeFavoriteMovie(movie)
                }
                roomDB.UserDao().updateUser(actualUser.toUserEntity())
            }
            observers.forEach {
                it.onFavoriteUpdate(actualUser.getFavoriteMovies())
            }
        }
    }

    private fun findMovie(movieName: String): Movie? {
        var movie: Movie? = null
        movies.forEach {
            if (it.title.equals(movieName)) {
                movie = it
            }

        }
        return movie?.copy()
    }

    fun addAllMovies(allMovies: List<Movie>) {
        movies.clear()
        movies.addAll(allMovies)
    }

    fun addObserver(observer: MdbDataObservers) {
        observers.clear()
        observers.add(observer)
    }
    fun removeObserver(observer: MdbDataObservers){
        observers.remove(observer)
    }
}