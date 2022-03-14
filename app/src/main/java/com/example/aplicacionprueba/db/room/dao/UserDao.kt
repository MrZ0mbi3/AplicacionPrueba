package com.example.aplicacionprueba.db.room.dao

import androidx.room.*
import com.example.aplicacionprueba.db.room.entity.UserEntity
import com.example.aplicacionprueba.db.room.entity.UserFavoriteMovies

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE userName = :name")
    suspend fun getUserByName(name:String):UserEntity

    @Insert
    suspend fun insertUser(user : UserEntity)

    @Update
    suspend fun updateUser(user:UserEntity)

    @Transaction
    @Query("SELECT * FROM UserEntity")
    fun getFavoriteMovieList(): List<UserFavoriteMovies>

}