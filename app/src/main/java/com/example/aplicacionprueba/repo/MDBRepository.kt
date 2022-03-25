package com.example.aplicacionprueba.repo

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.aplicacionprueba.MdbApplication
import com.example.aplicacionprueba.controller.MdbApi
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.db.room.MdbDataBaseRoom
import com.example.aplicacionprueba.db.room.mapper.toMovie
import com.example.aplicacionprueba.db.room.mapper.toUser
import com.example.aplicacionprueba.db.room.mapper.toUserEntity
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.model.User
import com.example.aplicacionprueba.vista.FavoriteMoviesFragment
import kotlinx.coroutines.*

class MDBRepository(private val localSource: MDBRepositoryLocal, private val remoteSource: MDBRepositoryRemote) {

    suspend fun getActualMovies(path: String): List<Movie> = withContext(Dispatchers.IO){
        val movies = remoteSource.getActualMovies(path)
        localSource.saveActualMovies(movies)
        movies
    }

    suspend fun getFavoriteMovies(path: String): List<Movie>  {
        return remoteSource.getFavoriteMovies(path)
    }

    //Log in Functions
    suspend fun autenticarUsuario(userName: String, userPassword: String){
        remoteSource.autenticarUsuario(userName,userPassword)
    }
    suspend fun verificarUsuario(name: String){
        localSource.verificarUsuario(name)
    }

    //mdbApi
    suspend fun adicionarAlUsuarioMovies(movies:MutableList<Movie>){
        localSource.adicionarAlUsuarioMovies(movies)
    }
    suspend fun actualizarPeliculaFavorita(movie: Movie, isChecked:Boolean){
        localSource.actualizarPeliculaFavorita(movie,isChecked)
        remoteSource.actualizarPeliculaFavorita(movie,isChecked)

    }

}

interface MDBRepositoryLocal {
    suspend fun saveActualMovies(movies: List<Movie>) : Boolean
    suspend fun verificarUsuario(name: String)
    suspend fun adicionarAlUsuarioMovies(movies:MutableList<Movie>)
    suspend fun actualizarPeliculaFavorita(movie: Movie, isChecked:Boolean)
}

class MDBRepositoryLocalImp(appContext: Context) : MDBRepositoryLocal {
    val data: MdbData by lazy { MdbData(roomDB) }
    val roomDB by lazy {
        Room.databaseBuilder(appContext, MdbDataBaseRoom::class.java, "DB").build()
    }

    override suspend fun saveActualMovies(movies: List<Movie>): Boolean {
        withContext(Dispatchers.IO){
            data.addAllMovies(movies)
            //delay(1000)
            movies.forEach {
                if (data.actualUser.getFavoriteMovies().contains(it)) {
                    it.isSelected = true
                }
            }
        }
        return true
    }

    override suspend fun verificarUsuario(name: String) {
        withContext(Dispatchers.IO){
            val mdbData = data
            val mdbRoom = roomDB


            if (mdbRoom.UserDao().getUserByName(name) != null) {

                val currentUser = mdbRoom.UserDao().getUserByName(name)
                val favorites = mdbRoom.UserDao().getFavoriteMovieList()
                    .first { it.user.userName == currentUser.userName }.favoritos.map {
                        it.toMovie(
                            currentUser.userName
                        )
                    }
                mdbData.actualUser =
                    currentUser.toUser(mutableSetOf<Movie>().also { it.addAll(favorites) })
                Log.d("Sign in", "User is not new" + mdbData.actualUser.userName)
            } else {
                val newUser = User(name, mutableSetOf())

                mdbRoom.UserDao().insertUser(newUser.toUserEntity())
                val currentUser = mdbRoom.UserDao().getUserByName(name)
                val favorites = mdbRoom.UserDao().getFavoriteMovieList()
                    .first { it.user.userName == currentUser.userName }.favoritos.map {
                        it.toMovie(
                            currentUser.userName
                        )
                    }
                mdbData.actualUser =
                    currentUser.toUser(mutableSetOf<Movie>().also { it.addAll(favorites) })
                Log.d("Sign in", "User is new" + mdbData.actualUser.userName)
            }
        }
    }

    override suspend fun adicionarAlUsuarioMovies(movies: MutableList<Movie>) {
        withContext(Dispatchers.IO){
            data.actualUser.addAll(movies)
            roomDB.UserDao().updateUser(data.actualUser.toUserEntity())
        }
    }

    override suspend fun actualizarPeliculaFavorita(movie: Movie, isChecked: Boolean) {
        withContext(Dispatchers.IO){
            data.updateFavoriteMovie(movie, isChecked)
        }
    }
}

interface MDBRepositoryRemote {
    suspend fun getActualMovies(path: String) : List<Movie>
    suspend fun getFavoriteMovies(path: String) : List<Movie>
    suspend fun autenticarUsuario(userName: String, userPassword: String)
    suspend fun actualizarPeliculaFavorita(movie: Movie, isChecked:Boolean)
}

class MDBRepositoryRemoteImpl(appContext: Context) : MDBRepositoryRemote {

    val api : MdbApi by lazy { MdbApi() }

    override suspend fun getActualMovies(path: String) : List<Movie>{
       return  api.getActualMovies(path)
    }

    override suspend fun getFavoriteMovies(path: String) : List<Movie> {
        return api.getFavoriteMovies(path)
    }

    override suspend fun autenticarUsuario(userName: String, userPassword: String) {
        api.requestAuthToken("3/authentication/token/new?api_key=267e487850dbcabfc3958c5f0b40bb10",
            "3/authentication/token/validate_with_login?api_key=267e487850dbcabfc3958c5f0b40bb10",
            userName,
            userPassword)
    }

    override suspend fun actualizarPeliculaFavorita(movie: Movie, isChecked: Boolean) {
        api.setFavoriteMovie("3/account/{account_id}/favorite?api_key=267e487850dbcabfc3958c5f0b40bb10&session_id=" + api.sessionId, movie,isChecked)
    }
}