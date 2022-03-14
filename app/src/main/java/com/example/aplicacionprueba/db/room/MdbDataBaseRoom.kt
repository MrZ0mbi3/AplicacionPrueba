package com.example.aplicacionprueba.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aplicacionprueba.db.room.dao.MovieDao
import com.example.aplicacionprueba.db.room.dao.UserDao
import com.example.aplicacionprueba.db.room.entity.MovieEntity
import com.example.aplicacionprueba.db.room.entity.UserEntity

@Database(
    entities = [UserEntity::class,MovieEntity::class],
    version = 3
)
//@TypeConverters(Converters::class)
abstract class MdbDataBaseRoom: RoomDatabase(){
    abstract fun UserDao():UserDao
    abstract fun MovieDao():MovieDao
}