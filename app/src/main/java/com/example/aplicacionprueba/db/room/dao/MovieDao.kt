package com.example.aplicacionprueba.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aplicacionprueba.db.room.entity.MovieEntity
import com.example.aplicacionprueba.model.Movie
@Dao
interface MovieDao {
    @Query("SELECT * FROM MovieEntity")
    suspend fun getAllMovies():List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id:String): MovieEntity

    @Insert
    suspend fun insertMovies(movies : List<MovieEntity>)
    @Insert
    suspend fun insertMovie(movies : MovieEntity)
}