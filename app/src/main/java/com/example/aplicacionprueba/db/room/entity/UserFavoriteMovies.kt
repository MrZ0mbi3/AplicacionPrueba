package com.example.aplicacionprueba.db.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class UserFavoriteMovies(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userName",
        entityColumn = "userNameCreator"
    )
    val favoritos: List<MovieEntity>
)